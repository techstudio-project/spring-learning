package com.techstudio.socket.nio;

import com.techstudio.socket.tcp.TCPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 基于阻塞io改造的，依然用了阻塞io中的线程模型，即每个客户端都会有两个线程（一读、一写）
 * 仅仅是为了使用nio中的一些api，并没有体现nio带来的性能提升
 *
 * @author lj
 * @since 2020/4/2
 */
public class NIOTCPServer extends TCPServer {

    private static final Logger logger = LoggerFactory.getLogger(NIOTCPServer.class);
    private Selector selector;
    private ServerSocketChannel serverChannel;

    public NIOTCPServer(int port) {
        super(port);
    }

    @Override
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
}
