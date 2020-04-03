package com.techstudio.socket.nio;

import com.techstudio.socket.tcp.ServerSocketListener;
import com.techstudio.socket.tcp.SocketHandler;
import com.techstudio.socket.tcp.TCPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author lj
 * @since 2020/4/2
 */
public class NIOServerSocketListener extends ServerSocketListener {

    private static final Logger logger = LoggerFactory.getLogger(NIOServerSocketListener.class);

    private final NIOTCPServer niotcpServer;

    public NIOServerSocketListener(TCPServer server) throws IOException {
        super(server);
        niotcpServer = (NIOTCPServer) server;
    }

    @Override
    public void run() {

        Selector selector = niotcpServer.getSelector();

        logger.info("服务端已启动。。。");

        do {
            try {
                // 选择已经就绪的通道（针对向选择器注册的关注事件通道）
                // 这是阻塞的，返回值为就绪的通道数量
                if (selector.select() == 0) {
                    // 没有就绪的通道，有可能是退出操作唤醒的select
                    if (exit) {
                        break;
                    }
                    continue;
                }

                // 拿到就绪通道的注册令牌
                // 一个SelectionKey键表示了一个特定的通道对象和一个特定的选择器对象之间的注册关系
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    if (exit) {
                        break;
                    }
                    SelectionKey key = iterator.next();
                    // 为什么要移除，需要研究研究
                    iterator.remove();

                    // 判断是否是需要的事件
                    if (key.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        // 这里就是非阻塞的
                        SocketChannel sc = ssc.accept();

                        // 启动SocketHandler进行异步处理
                        SocketHandler socketHandler = new NIOSocketHandler(niotcpServer, sc);
                        socketHandler.start();
                        socketHandler.setClientInfo(sc.getRemoteAddress().toString());

                        synchronized (niotcpServer) {
                            // 加入已连接的队列
                            niotcpServer.addSocketHandler(socketHandler);
                        }

                        logger.info("新客户端加入：{}", socketHandler.getClientInfo());
                    }
                }

            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

        } while (!exit);

        logger.info("服务端已关闭。。。");
    }
}
