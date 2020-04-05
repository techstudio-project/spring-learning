package com.techstudio.socket.core.support.dispatcher;

import com.techstudio.socket.core.AbstractSendPacket;
import com.techstudio.socket.core.IOArgs;
import com.techstudio.socket.core.SendDispatcher;
import com.techstudio.socket.core.Sender;
import com.techstudio.socket.core.util.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lj
 * @since 2020/4/4
 */
public class SendDispatcherAsync implements SendDispatcher, IOArgs.IOArgsEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SendDispatcherAsync.class);
    private final Sender sender;
    private final Queue<AbstractSendPacket> sendPacketQueue = new ConcurrentLinkedDeque<>();
    private final AtomicBoolean sending = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);

    // 当前需要发送的packet
    private AbstractSendPacket<?> currentSendPacketTemp;
    // 当前packet的大小
    private long total;
    // 当前packet已经发送的位置，进度
    private long position;

    private IOArgs ioArgs = new IOArgs();
    private ReadableByteChannel readableByteChannel;

    public SendDispatcherAsync(Sender sender) {
        this.sender = sender;
        this.sender.setSendListener(this);
    }

    @Override
    public void send(AbstractSendPacket sendPacket) {
        // 将packet放入队列
        sendPacketQueue.offer(sendPacket);
        // 判断是否是由空闲转为发送状态
        if (sending.compareAndSet(false, true)) {
            sendPackage();
        }
    }


    @Override
    public void cancel(AbstractSendPacket sendPacket) {

    }

    private void sendPackage() {
        // 上一个packet
        AbstractSendPacket preSendPacket = currentSendPacketTemp;
        if (preSendPacket != null) {
            CloseableUtils.close(logger, preSendPacket);
        }

        AbstractSendPacket sendPacket = currentSendPacketTemp = takePacket();
        if (sendPacket == null) {
            // 为null，说明队列中已经没有要发送的packet了，所以将发送状态置为false
            sending.set(false);
            return;
        }
        // 设置本次发送的总长度
        total = sendPacket.getLength();
        // 设置本次发送的起始位置
        position = 0;
        sendCurrentPacket();
    }

    private void sendCurrentPacket() {
        // 当前packet已经发送完了
        if (position >= total) {
            completeSendPacket(position == total);
            // 发送下一条
            sendPackage();
            return;
        }
        try {
            sender.postSendAsync();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void completeSendPacket(boolean success) {
        AbstractSendPacket sendPacket = this.currentSendPacketTemp;
        if (sendPacket == null) {
            return;
        }
        CloseableUtils.close(logger, sendPacket, readableByteChannel);
        currentSendPacketTemp = null;
        readableByteChannel = null;
        total = 0;
        position = 0;
    }

    private AbstractSendPacket takePacket() {
        AbstractSendPacket sendPacket = sendPacketQueue.poll();
        if (sendPacket != null && sendPacket.isCanceled()) {
            // 如果packet取消了发送，则继续从队列中拿packet
            return takePacket();
        }
        return sendPacket;
    }

    @Override
    public void close() throws IOException {
        if (closed.compareAndSet(false, true)) {
            sending.set(true);
            completeSendPacket(false);
        }
    }

    @Override
    public IOArgs provideIoArgs() {
        IOArgs args = this.ioArgs;

        // 第一次打开通道
        if (readableByteChannel == null) {
            readableByteChannel = Channels.newChannel(currentSendPacketTemp.open());
            // 将文件长度放到头部
            args.setLimit(4);
            args.writePacketLength((int) currentSendPacketTemp.getLength());
        } else {
            args.setLimit((int) Math.min(args.getCapacity(), total - position));
            try {
                int count = args.readFrom(readableByteChannel);
                position += count;
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }
        return args;
    }

    @Override
    public void onConsumeFailed(IOArgs args, Exception e) {
        logger.error(e.getMessage(), e);
    }

    @Override
    public void onConsumeCompleted(IOArgs args) {

    }
}
