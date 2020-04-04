package com.techstudio.socket.nio;

import com.techstudio.socket.core.Connector;
import com.techstudio.socket.tcp.SocketHandler;
import com.techstudio.socket.tcp.SocketHandlerCallback;
import com.techstudio.socket.util.IOContextUtils;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author lj
 * @since 2020/4/4
 */
public class NIOSocketHandlerNew extends SocketHandler {

    private final SocketChannel socketChannel;
    private final Connector connector;

    public NIOSocketHandlerNew(Socket socket, SocketHandlerCallback socketHandlerCallback) {
        super(socket, socketHandlerCallback);
        throw new UnsupportedOperationException();
    }

    public NIOSocketHandlerNew(SocketHandlerCallback socketHandlerCallback, SocketChannel socketChannel) throws IOException {
        super(socketChannel.socket(), socketHandlerCallback);
        this.socketChannel = socketChannel;
        connector = new Connector(socketChannel, IOContextUtils.getIoContext()) {
            @Override
            public void onReceiveMessage(String msg) {
                super.onReceiveMessage(msg);
                // 转发其它客户端
                socketHandlerCallback.onMessageReceive(NIOSocketHandlerNew.this, msg);
            }
        };
    }

    @Override
    public void start() throws IOException {
        if (socketWriteHandler == null) {
            Selector writeSelector = Selector.open();
            socketChannel.register(writeSelector, SelectionKey.OP_WRITE);
            socketWriteHandler = new NIOSocketWriteHandler(this, writeSelector, socketChannel);
        }
    }
}
