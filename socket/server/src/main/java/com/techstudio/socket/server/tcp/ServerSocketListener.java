package com.techstudio.socket.server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author lj
 * @since 2020/3/31
 */
public class ServerSocketListener extends Thread {

    private final TCPServer tcpServer;
    private ServerSocket serverSocket;
    protected boolean exit = false;

    public ServerSocketListener(TCPServer tcpServer) throws IOException {
        super("socket-listener-thread");
        this.tcpServer = tcpServer;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(tcpServer.getPort());
            initServerSocket(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("服务端已启动。。。");
        do {
            Socket socket;
            try {
                socket = serverSocket.accept();
                // 启动SocketHandler进行异步处理
                SocketHandler socketHandler = new SocketHandler(socket, tcpServer);
                socketHandler.start();
                socketHandler.setClientInfo(socket.getInetAddress().toString() + ":"
                        + socket.getPort());

                synchronized (tcpServer) {
                    // 加入已连接的队列
                    tcpServer.addSocketHandler(socketHandler);
                }

                System.out.println("新客户端加入：" + socketHandler.getClientInfo());
            } catch (IOException e) {
                continue;
            }

        } while (!exit);
        System.out.println("服务端已关闭。。。");
    }

    public void exit() {
        exit = true;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initServerSocket(ServerSocket ss) throws SocketException {
        // 是否复用未完全关闭的地址端口
        ss.setReuseAddress(true);

        // 接收缓冲区大小，等效 Socket#setReceiveBufferSize
        ss.setReceiveBufferSize(64 * 1024);

        // 设置ServerSocket#accept超时时间，一般不设置
        // defaultServerSocket.setSoTimeout(2000);

        // 设置性能参数：短链接，延迟，带宽的相对重要性
        ss.setPerformancePreferences(1, 1, 1);
    }

    public TCPServer getTcpServer() {
        return tcpServer;
    }
}
