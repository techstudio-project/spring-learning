package com.techstudio.socket.core.support;

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
public class SendDispatcherAsync implements SendDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(SendDispatcherAsync.class);
    private final Sender sender;
    private final Queue<AbstractSendPacket> sendPacketQueue = new ConcurrentLinkedDeque<>();
    private final AtomicBoolean sending = new AtomicBoolean(false);

    // 当前需要发送的packet
    private AbstractSendPacket currentSendPacket;
    // 当前packet的大小
    private int total;
    // 当前packet已经发送的位置，进度
    private int position;

    public SendDispatcherAsync(Sender sender) {
        this.sender = sender;
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
        AbstractSendPacket preSendPacket = currentSendPacket;
        if (preSendPacket != null) {
            CloseableUtils.close(logger, preSendPacket);
        }

        AbstractSendPacket sendPacket = currentSendPacket = takePacket();
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
        IOArgs ioArgs = new IOArgs();

        ioArgs.startWriting();

        // 当前packet已经发送完了
        if (position >= total) {
            // 发送下一条
            sendPackage();
            return;
        } else if (position == 0) {
            // 刚开始发送，需要携带长度信息，（针对与socketChannel而言的首包）
            ioArgs.writePacketLength(total);
        }

        byte[] bytes = currentSendPacket.getBytes();
        int count = ioArgs.readFrom(bytes, position);
        position += count;

        ioArgs.finishWriting();

        try {
            sender.sendAsync(ioArgs, ioArgsEventListener);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private AbstractSendPacket takePacket() {
        AbstractSendPacket sendPacket = sendPacketQueue.poll();
        if (sendPacket != null && sendPacket.isCanceled()) {
            // 如果packet取消了发送，则继续从队列中拿packet
            return takePacket();
        }
        return sendPacket;
    }

    private IOArgs.IOArgsEventListener ioArgsEventListener = new IOArgs.IOArgsEventListener() {
        @Override
        public void onStarted(IOArgs args) {

        }

        @Override
        public void onCompleted(IOArgs args) {
            // 当前包长度可能大于byteBuffer的容量，此时需要继续发送
            sendCurrentPacket();
        }
    };

    @Override
    public void close() throws IOException {

    }
}
