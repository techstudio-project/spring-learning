package com.techstudio.socket.server.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lj
 * @since 2020/4/2
 */
public class NIOTCPServer implements NIOSocketHandlerCallback {

    private static final Logger logger = LoggerFactory.getLogger(NIOTCPServer.class);
    private Selector selector;
    private ServerSocketChannel serverChannel;

    private final int port;
    private List<NIOSocketHandlerNew> socketHandlers = new ArrayList<>();
    private NIOServerSocketListener serverListener;

    protected ExecutorService forwardingThreadPoolExecutor = Executors.newSingleThreadExecutor();

    public NIOTCPServer(int port) {
        this.port = port;
    }

    public NIOTCPServer() {
        this(8443);
    }

    public boolean start() {
        try {
            // 创建Selector（选择器）对象，NIO的核心组件之一，用于（轮询）检查多个nio channel状态是否可读、可写。
            // 使用Selector的好处在于：使用更少的线程处理通道，相比于bio，节省线程的开销，以及线程上下文切换带来的开销
            selector = Selector.open();

            // 类似于ServerSocket，监听新进来的TCP连接的通道
            serverChannel = ServerSocketChannel.open();

            // channel必须为非阻塞
            serverChannel.configureBlocking(false);

            // 绑定到本地ip+端口
            serverChannel.socket().bind(new InetSocketAddress(port));

            // 注册客户端连接到达的监听
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            // 启动监听线程
            serverListener = new NIOServerSocketListener(this);
            serverListener.start();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public Selector getSelector() {
        return selector;
    }

    public ServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    @Override
    public void onClosed(NIOSocketHandlerNew socketHandler) {

    }

    @Override
    public void onMessageReceive(NIOSocketHandlerNew socketHandler, String msg) {
        // 通过异步线程池给其它在线客户端发送消息
        forwardingThreadPoolExecutor.execute(() -> {
            synchronized (this) {
                for (NIOSocketHandlerNew handler : socketHandlers) {
                    if (socketHandler.equals(handler)) {
                        continue;
                    }
                    handler.send(msg);
                }
            }
        });
    }

    /**
     * 广播消息
     *
     * @param msg msg
     */
    public synchronized void broadcast(String msg) {
        for (NIOSocketHandlerNew socketHandler : socketHandlers) {
            socketHandler.send(msg);
        }
    }

    public synchronized void removeSocketHandler(NIOSocketHandlerNew socketHandler) {
        this.socketHandlers.remove(socketHandler);
    }

    public synchronized void addSocketHandler(NIOSocketHandlerNew socketHandler) {
        this.socketHandlers.add(socketHandler);
    }

    public int getPort() {
        return port;
    }

    public void stop() {
    }

}
