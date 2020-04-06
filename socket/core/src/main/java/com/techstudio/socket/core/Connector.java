package com.techstudio.socket.core;

import com.techstudio.socket.core.support.*;
import com.techstudio.socket.core.support.dispatcher.ReceiveDispatcherAsync;
import com.techstudio.socket.core.support.dispatcher.SendDispatcherAsync;
import com.techstudio.socket.core.support.packet.*;
import com.techstudio.socket.core.util.IOContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.UUID;

import static com.techstudio.socket.core.AbstractPacket.*;

/**
 * 客户端与服务端的交互抽象为一个连接
 *
 * @author lj
 * @since 2020/4/4
 */
public abstract class Connector implements SocketChannelAdapter.ChannelStatusChangedListener {

    private static final Logger logger = LoggerFactory.getLogger(Connector.class);
    // 连接的唯一标识
    private final UUID key = UUID.randomUUID();
    private final SocketChannel channel;

    private SendDispatcher sendDispatcher;
    private ReceiveDispatcher receiveDispatcher;

    public Connector(SocketChannel channel) {
        this.channel = channel;
    }

    public void setup() throws IOException {
        SocketChannelAdapter adapter = new SocketChannelAdapter(
                channel, IOContextUtils.getIoContext().getIoProvider(), this);

        sendDispatcher = new SendDispatcherAsync(adapter);
        receiveDispatcher = new ReceiveDispatcherAsync(adapter, receivePacketCallback);
        receiveDispatcher.start();
    }

    public void send(String msg) {
        AbstractSendPacket sendPacket = new StringSendPacket(msg);
        sendDispatcher.send(sendPacket);
    }

    public void send(AbstractSendPacket sendPacket) {
        sendDispatcher.send(sendPacket);
    }

    @Override
    public void onStatusChanged(SocketChannel channel) {

    }

    public void onReceiveMessage(String msg) {
        logger.info("{}:{}", key, msg);
    }

    protected void onReceivedPacket(AbstractReceivePacket receivePacket) {
        logger.info("{}:[New Packet]-Type:{},Length:{}", key, receivePacket.getType(), receivePacket.getLength());
    }


    private ReceiveDispatcher.ReceivePacketCallback receivePacketCallback = new ReceiveDispatcher.ReceivePacketCallback() {
        @Override
        public AbstractReceivePacket<?, ?> onArrivedNewPacket(byte type, long length) {
            switch (type) {
                case TYPE_MEMORY_BYTES:
                case TYPE_STREAM_DIRECT:
                    return new BytesReceivePacket(length);
                case TYPE_MEMORY_STRING:
                    return new StringReceivePacket(length);
                case TYPE_STREAM_FILE:
                    return new FileReceivePacket(length, createNewReceiveFile());
                default:
                    throw new UnsupportedOperationException("Unsupported packet type:" + type);
            }
        }

        @Override
        public void onReceivePacketCompleted(AbstractReceivePacket receivePacket) {
            onReceivedPacket(receivePacket);
        }
    };

    protected abstract File createNewReceiveFile();

    public UUID getKey() {
        return key;
    }
}
