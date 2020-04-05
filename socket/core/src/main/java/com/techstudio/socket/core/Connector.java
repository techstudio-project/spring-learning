package com.techstudio.socket.core;

import com.techstudio.socket.core.support.*;
import com.techstudio.socket.core.util.IOContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * 客户端与服务端的交互抽象为一个连接
 *
 * @author lj
 * @since 2020/4/4
 */
public class Connector implements SocketChannelAdapter.ChannelStatusChangedListener {

    private static final Logger logger = LoggerFactory.getLogger(Connector.class);
    // 连接的唯一标识
    private final UUID key = UUID.randomUUID();
    private final SocketChannel channel;
    private Sender sender;
    private Receiver receiver;

    private SendDispatcher sendDispatcher;
    private ReceiveDispatcher receiveDispatcher;

    public Connector(SocketChannel channel) {
        this.channel = channel;
    }

    public void setup() throws IOException {
        SocketChannelAdapter adapter = new SocketChannelAdapter(channel, IOContextUtils.getIoContext().getIoProvider(), this);
        this.sender = adapter;
        this.receiver = adapter;

        sendDispatcher = new SendDispatcherAsync(sender);
        receiveDispatcher = new ReceiveDispatcherAsync(receiver, receivePacketCallback);
        receiveDispatcher.start();
    }

    public void send(String msg) {
        AbstractSendPacket sendPacket = new StringSendPacket(msg);
        sendDispatcher.send(sendPacket);
    }

    @Override
    public void onStatusChanged(SocketChannel channel) {

    }

    public void onReceiveMessage(String msg) {
        logger.info("{}:{}", key, msg);
    }

    private ReceiveDispatcher.ReceivePacketCallback receivePacketCallback = receivePacket -> {
        if (receivePacket instanceof StringReceivePacket) {
            String msg = ((StringReceivePacket) receivePacket).string();
            onReceiveMessage(msg);
        }
    };

    public UUID getKey() {
        return key;
    }
}
