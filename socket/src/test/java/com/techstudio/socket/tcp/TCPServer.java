package com.techstudio.socket.tcp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lj
 * @since 2020/3/31
 */
public class TCPServer implements SocketHandlerCallback {

    protected final int port;
    protected List<SocketHandler> socketHandlers = new ArrayList<>();
    protected ServerSocketListener serverListener;
    protected ExecutorService forwardingThreadPoolExecutor = Executors.newSingleThreadExecutor();

    public TCPServer(int port) {
        this.port = port;
    }

    public TCPServer() {
        this(8443);
    }

    public boolean start() {
        try {
            // 开启服务端监听线程，监听客户端socket连接
            serverListener = new ServerSocketListener(this);
            serverListener.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stop() {
        // 关闭
        if (serverListener != null) {
            serverListener.exit();
        }

        synchronized (this) {
            // 关闭所有建立连接的socket
            for (SocketHandler socketHandler : socketHandlers) {
                socketHandler.exit();
            }
            // 清空客户端连接队列
            socketHandlers.clear();
        }

        // 结束线程池
        forwardingThreadPoolExecutor.shutdownNow();
    }

    /**
     * 广播消息
     *
     * @param msg msg
     */
    public synchronized void broadcast(String msg) {
        for (SocketHandler socketHandler : socketHandlers) {
            socketHandler.send(msg);
        }
    }

    public synchronized void removeSocketHandler(SocketHandler socketHandler) {
        this.socketHandlers.remove(socketHandler);
    }

    public synchronized void addSocketHandler(SocketHandler socketHandler) {
        this.socketHandlers.add(socketHandler);
    }

    public int getPort() {
        return port;
    }

    @Override
    public synchronized void onClosed(SocketHandler socketHandler) {
        this.socketHandlers.remove(socketHandler);
    }

    @Override
    public void onMessageReceive(final SocketHandler socketHandler, final String msg) {
        // 通过异步线程池给其它在线客户端发送消息
        forwardingThreadPoolExecutor.execute(() -> {
            synchronized (this) {
                for (SocketHandler handler : socketHandlers) {
                    if (socketHandler.equals(handler)) {
                        continue;
                    }
                    handler.send(msg);
                }
            }
        });
    }
}
