package com.techstudio.socket.core.support.dispatcher;

import com.techstudio.socket.core.AbstractSendPacket;
import com.techstudio.socket.core.IOArgs;
import com.techstudio.socket.core.SendDispatcher;
import com.techstudio.socket.core.Sender;
import com.techstudio.socket.core.util.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lj
 * @since 2020/4/4
 */
public class SendDispatcherAsync implements SendDispatcher, IOArgs.IOArgsEventProcessor,
        PacketReaderAsync.SendPacketProvider {

    private static final Logger logger = LoggerFactory.getLogger(SendDispatcherAsync.class);
    private final Sender sender;
    private final Queue<AbstractSendPacket> sendPacketQueue = new ConcurrentLinkedDeque<>();
    private final Object queueLock = new Object();
    private final PacketReaderAsync packetReader = new PacketReaderAsync(this);

    private final AtomicBoolean sending = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public SendDispatcherAsync(Sender sender) {
        this.sender = sender;
        this.sender.setSendListener(this);
    }

    @Override
    public void send(AbstractSendPacket sendPacket) {
        synchronized (queueLock) {
            // 将packet放入队列
            sendPacketQueue.offer(sendPacket);
            // 判断是否是由空闲转为发送状态
            if (sending.compareAndSet(false, true)) {
                // 请求获取一个包
                if (packetReader.requestTakePacket()) {
                    // 如果拿到了，就发起发送请求
                    requestSend();
                }
            }
        }
    }

    @Override
    public void cancel(AbstractSendPacket sendPacket) {
        boolean ret;
        synchronized (queueLock) {
            ret = sendPacketQueue.remove(sendPacket);
        }
        if (ret) {
            sendPacket.cancel();
            return;
        }
        packetReader.cancel(sendPacket);
    }

    /**
     * 请求网络进行数据发送
     */
    private void requestSend() {
        try {
            sender.postSendAsync();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public AbstractSendPacket takePacket() {
        AbstractSendPacket sendPacket;
        synchronized (queueLock) {
            sendPacket = sendPacketQueue.poll();
            if (sendPacket == null) {
                sending.set(false);
                return null;
            }
        }
        if (sendPacket.isCanceled()) {
            // 如果packet取消了发送，则继续从队列中拿packet
            return takePacket();
        }
        return sendPacket;
    }

    @Override
    public void completedPacket(AbstractSendPacket packet, boolean isSucceed) {
        CloseableUtils.close(logger, packet);
    }

    @Override
    public void close() throws IOException {
        if (closed.compareAndSet(false, true)) {
            sending.set(true);
            packetReader.close();
        }
    }

    @Override
    public IOArgs provideIoArgs() {
        return packetReader.fillData();
    }

    @Override
    public void onConsumeFailed(IOArgs args, Exception e) {
        if (args != null) {
            logger.error(e.getMessage(), e);
        } else {
            // todo
        }

    }

    @Override
    public void onConsumeCompleted(IOArgs args) {
        if (packetReader.requestTakePacket()) {
            requestSend();
        }
    }
}
