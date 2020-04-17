package com.techstudio.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lj
 * @since 2020/4/6
 */
public class NettyApp {

    private static final Logger logger = LoggerFactory.getLogger(NettyApp.class);

    // 64K
    private static final int MAX_CONTENT_LENGTH = 64 * 1024;

    private static EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();
    private static NioServerSocketChannel serverSocketChannel;

    public static void main(String[] args) {

        registerAppShutdownHook();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 加入自己业务逻辑的地方
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 解码成HttpRequest
                            pipeline.addLast(new HttpServerCodec());
                            // 解码成FullHttpRequest
                            pipeline.addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH));
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                            pipeline.addLast(new WebSocketChannelInboundHandler());
                        }
                    });

            // 绑定服务端ip+端口，实际返回的是NioServerSocketChannel，即上面我们注册的channel
            serverSocketChannel = (NioServerSocketChannel) bootstrap.bind(40000).sync().channel();

            // 当客户端与服务端不再连接，将关闭所有的channel（包括socketChannel和serverSocketChannel）
            serverSocketChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static void registerAppShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(NettyApp::shutdown));
    }

    private static void shutdown() {
        if (serverSocketChannel != null) {
            serverSocketChannel.close();
            logger.info("服务器已关闭");
        }

    }

}
