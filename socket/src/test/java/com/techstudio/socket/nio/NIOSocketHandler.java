package com.techstudio.socket.nio;

import com.techstudio.socket.tcp.SocketHandler;
import com.techstudio.socket.tcp.SocketHandlerCallback;
import com.techstudio.socket.tcp.SocketReadHandler;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author lj
 * @since 2020/4/2
 */
public class NIOSocketHandler extends SocketHandler {

    private final SocketChannel socketChannel;

    public NIOSocketHandler(Socket socket, SocketHandlerCallback socketHandlerCallback) {
        super(socket, socketHandlerCallback);
        throw new UnsupportedOperationException();
    }

    public NIOSocketHandler(SocketHandlerCallback socketHandlerCallback, SocketChannel socketChannel) {
        super(socketChannel.socket(), socketHandlerCallback);
        this.socketChannel = socketChannel;
    }

    @Override
    public void start() throws IOException {
        // channel必须为非阻塞
        socketChannel.configureBlocking(false);

        if (socketReadHandler == null) {
            Selector readSelector = Selector.open();
            socketChannel.register(readSelector, SelectionKey.OP_READ);
            socketReadHandler = new NIOSocketReadHandler(this, readSelector);
        }
        socketReadHandler.start();

        if (socketWriteHandler == null) {
            Selector writeSelector = Selector.open();
            socketChannel.register(writeSelector, SelectionKey.OP_WRITE);
            socketWriteHandler = new NIOSocketWriteHandler(this, writeSelector,socketChannel);
        }
    }
}
