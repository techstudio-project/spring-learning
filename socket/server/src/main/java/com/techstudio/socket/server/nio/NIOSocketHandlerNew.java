package com.techstudio.socket.server.nio;

import com.techstudio.socket.core.Connector;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @author lj
 * @since 2020/4/4
 */
public class NIOSocketHandlerNew extends Connector {

    private final NIOSocketHandlerCallback callback;

    public NIOSocketHandlerNew(SocketChannel channel, NIOSocketHandlerCallback callback) throws IOException {
        super(channel);
        setup();
        this.callback = callback;
    }

    public void exit() {

    }

    @Override
    public void onReceiveMessage(String msg) {
        super.onReceiveMessage(msg);
        callback.onMessageReceive(this, msg);
    }
}
