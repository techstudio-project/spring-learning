package com.techstudio.socket.core.support.dispatcher;

import com.techstudio.socket.core.AbstractSendPacket;
import com.techstudio.socket.core.Frame;
import com.techstudio.socket.core.IOArgs;
import com.techstudio.socket.core.support.ds.BytePriorityNode;
import com.techstudio.socket.core.support.frame.AbstractSendPacketFrame;
import com.techstudio.socket.core.support.frame.CancelSendFrame;
import com.techstudio.socket.core.support.frame.SendEntityFrame;
import com.techstudio.socket.core.support.frame.SendHeaderFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Packet转换为帧序列，并进行读取发送的封装管理类
 */
public class PacketReaderAsync implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(PacketReaderAsync.class);
    private final SendPacketProvider sendPacketProvider;

    private volatile IOArgs args = new IOArgs();
    // 链表头
    private volatile BytePriorityNode<Frame> node;
    private volatile int nodeSize = 0;

    // 每个packet的唯一标识，最大255 ，即packet的最大并发数为255
    private short lastIdentifier = 0;

    public PacketReaderAsync(SendPacketProvider sendPacketProvider) {
        this.sendPacketProvider = sendPacketProvider;
    }

    /**
     * 关闭当前Reader，关闭时应关闭所有的Frame对应的Packet
     */
    @Override
    public synchronized void close() {
        while (node != null) {
            Frame frame = node.getItem();
            if (frame instanceof AbstractSendPacketFrame) {
                AbstractSendPacket packet = ((AbstractSendPacketFrame) frame).getSendPacket();
                sendPacketProvider.completedPacket(packet, false);
            }
            node = node.getNext();
        }

        nodeSize = 0;
        node = null;
    }

    /**
     * 取消Packet对应的帧发送，如果当前Packet已发送部分数据（就算只是头数据）
     * 也应该在当前帧队列中发送一份取消发送的标志{@link CancelSendFrame}
     *
     * @param sendPacket 待取消的packet
     */
    public synchronized void cancel(AbstractSendPacket sendPacket) {
        if (nodeSize == 0) {
            return;
        }

        for (BytePriorityNode<Frame> x = node, before = null; x != null; before = x, x = x.getNext()) {
            Frame frame = x.getItem();
            if (frame instanceof AbstractSendPacketFrame) {
                AbstractSendPacketFrame packetFrame = (AbstractSendPacketFrame) frame;
                // 比较引用地址
                if (packetFrame.getSendPacket() == sendPacket) {
                    boolean removed = packetFrame.abort();
                    if (removed) {
                        // A B C
                        removeFrame(x, before);
                        if (packetFrame instanceof SendHeaderFrame) {
                            // 头帧，并且未被发送任何数据，直接取消后不需要添加取消发送帧
                            break;
                        }
                    }

                    // 已经发送了部分数据,添加终止帧，通知到接收方
                    CancelSendFrame cancelSendFrame = new CancelSendFrame(packetFrame.getBodyIdentifier());
                    appendNewFrame(cancelSendFrame);

                    // 意外终止，返回失败
                    sendPacketProvider.completedPacket(sendPacket, false);
                    break;
                }
            }
        }
    }

    /**
     * 从队列中拿一份packet进行发送
     * 如果当前reader中有可以进行网络发送的数据则返回true
     *
     * @return boolean
     */
    public boolean requestTakePacket() {
        synchronized (this) {
            // 当链表中有数据时，说明有数据正在发送，则不用去队列里再拿数据出来解析到frame里
            // 1 代表有数据
            // 后面可以设置大于1的数，表示虽然还有数据在发送此时我也可以去队列里拿新的packet，这样可以达到并发的目的
            if (nodeSize >= 4) {
                return true;
            }
        }
        AbstractSendPacket sendPacket = sendPacketProvider.takePacket();
        // 发送头帧
        if (sendPacket != null) {
            short identifier = generateIdentifier();
            SendHeaderFrame headerFrame = new SendHeaderFrame(identifier, sendPacket);
            appendNewFrame(headerFrame);
        }
        synchronized (this) {
            return nodeSize != 0;
        }
    }

    /**
     * 填充数据到IOArgs
     * 如果当前有可以发送的帧，则填充数据并返回，如果填充失败则返回null
     *
     * @return IOArgs
     */
    public IOArgs fillData() {
        Frame currentFrame = getCurrentFrame();
        if (currentFrame == null) {
            return null;
        }
        try {
            // 发送本帧数据
            if (currentFrame.handle(args)) {
                // 发送成功，则构建下一个帧
                Frame nextFrame = currentFrame.nextFrame();
                if (nextFrame != null) {
                    // 加入到node中
                    appendNewFrame(nextFrame);
                } else if (currentFrame instanceof SendEntityFrame) {
                    // 末尾实体帧,说明发送完成，通知回调
                    sendPacketProvider.completedPacket(((SendEntityFrame) currentFrame).getSendPacket(), true);
                }
                // 从node链头弹出
                popCurrentFrame();
            }
            return args;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 弹出链表头的帧
     */
    private synchronized void popCurrentFrame() {
        node = node.getNext();
        nodeSize--;
        if (node == null) {
            requestTakePacket();
        }
    }

    /**
     * 获取当前链表头的帧
     *
     * @return Frame
     */
    private synchronized Frame getCurrentFrame() {
        if (node == null) {
            return null;
        }
        return node.getItem();
    }

    /**
     * 添加新帧
     *
     * @param frame frame
     */
    private synchronized void appendNewFrame(Frame frame) {
        BytePriorityNode<Frame> newNode = new BytePriorityNode<>(frame);
        if (node != null) {
            // 使用优先级别添加到链表
            node.appendWithPriority(newNode);
        } else {
            node = newNode;
        }
        nodeSize++;
    }

    /**
     * 删除某帧对应的链表节点
     *
     * @param removeNode 待删除的节点
     * @param before     当前删除节点的前一个节点，用于构建新的链表结构
     */
    private synchronized void removeFrame(BytePriorityNode<Frame> removeNode, BytePriorityNode<Frame> before) {
        if (before == null) {
            // A B C
            // B C
            node = removeNode.getNext();
        } else {
            // A B C
            // A C
            before.setNext(removeNode.getNext());
        }
        nodeSize--;
        if (node == null) {
            requestTakePacket();
        }
    }

    /**
     * 构建一份Packet惟一标志
     *
     * @return 标志为：1～255
     */
    private short generateIdentifier() {
        short identifier = ++lastIdentifier;
        if (identifier == 255) {
            lastIdentifier = 0;
        }
        return identifier;
    }

    /**
     * Packet提供者，供SendDispatcherAsync回调
     */
    interface SendPacketProvider {
        /**
         * 拿Packet操作
         *
         * @return 如果队列有可以发送的Packet则返回不为null
         */
        AbstractSendPacket takePacket();

        /**
         * 结束一份Packet
         *
         * @param packet    发送包
         * @param isSucceed 是否成功发送完成
         */
        void completedPacket(AbstractSendPacket packet, boolean isSucceed);
    }
}
