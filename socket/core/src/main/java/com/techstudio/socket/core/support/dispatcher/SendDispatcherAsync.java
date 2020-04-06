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

    private final PacketReaderAsync packetReader = new PacketReaderAsync(this);

    private final AtomicBoolean sending = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public SendDispatcherAsync(Sender sender) {
        this.sender = sender;
        this.sender.setSendListener(this);
    }

    @Override
    public void send(AbstractSendPacket sendPacket) {
        // 将packet放入队列
        sendPacketQueue.offer(sendPacket);
        requestSend();
    }

    @Override
    public void cancel(AbstractSendPacket sendPacket) {
        boolean ret = sendPacketQueue.remove(sendPacket);
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

        synchronized (sending) {
            if (sending.get() || closed.get()) {
                return;
            }
            if (packetReader.requestTakePacket()) {
                try {
                    boolean success = sender.postSendAsync();
                    if (success) {
                        sending.set(true);
                    }
                } catch (IOException e) {
                    closeAndNotify();
                }
            }
        }

    }

    @Override
    public AbstractSendPacket takePacket() {
        AbstractSendPacket sendPacket = sendPacketQueue.poll();
        if (sendPacket == null) {
            return null;
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
    public void close() {
        if (closed.compareAndSet(false, true)) {
            packetReader.close();
            sendPacketQueue.clear();
            synchronized (sending) {
                sending.set(true);
            }
        }
    }

    @Override
    public IOArgs provideIoArgs() {
        return closed.get() ? null : packetReader.fillData();
    }

    @Override
    public void onConsumeFailed(IOArgs args, Exception e) {
        logger.error(e.getMessage(), e);
        synchronized (sending) {
            sending.set(false);
        }
        requestSend();
    }

    @Override
    public void onConsumeCompleted(IOArgs args) {
        synchronized (sending) {
            sending.set(false);
        }
        requestSend();
    }

    /**
     * 请求网络发送异常时触发，进行关闭操作
     */
    private void closeAndNotify() {
        CloseableUtils.close(logger, this);
    }
}
