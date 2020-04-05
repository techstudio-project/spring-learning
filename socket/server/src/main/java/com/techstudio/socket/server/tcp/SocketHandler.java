package com.techstudio.socket.server.tcp;

import java.io.IOException;
import java.net.Socket;

/**
 * @author lj
 * @since 2020/3/31
 */
public class SocketHandler {

    private final Socket socket;
    private final SocketHandlerCallback socketHandlerCallback;
    protected SocketReadHandler socketReadHandler;
    protected SocketWriteHandler socketWriteHandler;
    private String clientInfo;

    public SocketHandler(Socket socket, SocketHandlerCallback socketHandlerCallback) {
        this.socket = socket;
        this.socketHandlerCallback = socketHandlerCallback;
    }

    /**
     * 启动读写线程
     */
    public void start() throws IOException {
        if (socketReadHandler == null) {
            this.socketReadHandler = new SocketReadHandler(this);
        }
        if (socketWriteHandler == null) {
            this.socketWriteHandler = new SocketWriteHandler(this);
        }

        socketReadHandler.start();
    }

    public void exit() {
        if (socketReadHandler != null) {
            socketReadHandler.exit();
        }
        if (socketWriteHandler != null) {
            socketWriteHandler.exit();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitBySelf() {
        exit();
        // socket关闭回调
        socketHandlerCallback.onClosed(this);
    }

    public void send(String msg) {
        socketWriteHandler.send(msg);
    }

    public Socket getSocket() {
        return socket;
    }

    public SocketHandlerCallback getSocketHandlerCallback() {
        return socketHandlerCallback;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }
}
