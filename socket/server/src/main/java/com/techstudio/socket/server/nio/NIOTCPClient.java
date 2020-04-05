package com.techstudio.socket.server.nio;

import com.techstudio.socket.core.Connector;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

/**
 * @author lj
 * @since 2020/4/2
 */
public class NIOTCPClient extends Connector {

    public NIOTCPClient(int remotePort) throws IOException {
        this(remotePort, null);
    }

    public NIOTCPClient(int remotePort, Integer localPort) throws IOException {
        super(init(remotePort, localPort));
        setup();
    }

    public void close() {

    }

    private static SocketChannel init(int remotePort, Integer localPort) throws IOException {
        SocketChannel channel = SocketChannel.open();
        if (localPort != null) {
            channel.bind(new InetSocketAddress(InetAddress.getLocalHost(), localPort));
        }
        initSocket(channel.socket());
        channel.connect(new InetSocketAddress(InetAddress.getLocalHost(), remotePort));
        return channel;
    }

    private static void initSocket(Socket socket) throws SocketException {
        // 设置读取超时时间
        // socket.setSoTimeout(2000);

        // 是否复用未完全关闭的Socket地址，对于指定bind操作后的套接字有效
        socket.setReuseAddress(true);

        // 是否开启Nagle算法
        socket.setTcpNoDelay(true);

        // 是否需要在长时无数据响应时发送确认数据（类似心跳包），时间大约为2小时
        socket.setKeepAlive(true);

        // 对于close关闭操作行为进行怎样的处理；默认为false，0
        // false、0：默认情况，关闭时立即返回，底层系统接管输出流，将缓冲区内的数据发送完成
        // true、0：关闭时立即返回，缓冲区数据抛弃，直接发送RST结束命令到对方，并无需经过2MSL等待
        // true、200：关闭时最长阻塞200毫秒，随后按第二情况处理
        socket.setSoLinger(true, 20);

        // 是否让紧急数据内敛，默认false；紧急数据通过 socket.sendUrgentData(1);发送
        socket.setOOBInline(true);

        // 设置接收发送缓冲器大小
        socket.setReceiveBufferSize(64 * 1024);
        socket.setSendBufferSize(64 * 1024);

        // 设置性能参数：短链接，延迟，带宽的相对重要性
        socket.setPerformancePreferences(1, 1, 1);
    }

}
