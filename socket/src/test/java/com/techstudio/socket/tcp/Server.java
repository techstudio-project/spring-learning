package com.techstudio.socket.tcp;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

/**
 * @author lj
 * @since 2020/3/27
 */
public class Server {

    private static final int PORT = 8080;


    public static void main(String[] args) throws IOException {

        // 创建ServerSocket
        ServerSocket serverSocket = createServerSocket();

        // 初始化配置
        initServerSocket(serverSocket);

        // 绑定到ip：port
        serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), PORT), 50);

        System.out.println("server started，ip:" + serverSocket.getInetAddress().toString()
                + " port:" + serverSocket.getLocalPort());

        // 无限循环等待有新的客户端连接
        for (; ; ) {
            // 等待连接，会阻塞当前线程
            Socket client = serverSocket.accept();

            // 开启新线程异步执行与客户端的交互
            ClientHandlerThread1 handler = new ClientHandlerThread1(client);

            // 开始启动线程
            handler.start();
        }
    }

    private static class ClientHandlerThread extends Thread {

        private final Socket socket;
        private boolean exit = false;

        public ClientHandlerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String clientName = socket.getInetAddress().toString() + ":" + socket.getPort();
            System.out.println("新客户端连接：" + clientName);

            try {
                // 得到打印流，用于数据输出；服务器回送数据使用
                PrintStream printStream = new PrintStream(socket.getOutputStream());

                // 得到输入字符流，用于接收数据
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                do {
                    String str = bufferedReader.readLine();
                    if ("exit".equalsIgnoreCase(str)) {
                        exit = true;
                    } else {
                        // 将客户端发来的信息打印到控制台
                        System.out.println(clientName + ":" + str);
                        // 将字符串长度返回给客户端
                        printStream.println(str.length());
                    }
                } while (!exit);

                printStream.println("exit");

                printStream.close();
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("客户端已退出：" + clientName);
            }
        }
    }

    private static class ClientHandlerThread1 extends Thread {

        private final Socket socket;
        private boolean exit = false;

        public ClientHandlerThread1(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String clientName = socket.getInetAddress().toString() + ":" + socket.getPort();
            System.out.println("新客户端连接：" + clientName);
            try {

                // 输入流
                InputStream input = socket.getInputStream();
                byte[] buf = new byte[1024];
                int byteCount = input.read(buf);

                // 将读取到的字节数组包装成ByteBuffer
                ByteBuffer byteBuffer = ByteBuffer.wrap(buf, 0, byteCount);

                byte b = byteBuffer.get();

                char c = byteBuffer.getChar();

                int i = byteBuffer.getInt();

                boolean bool = byteBuffer.get() == 1;

                long l = byteBuffer.getLong();

                float f = byteBuffer.getFloat();

                double d = byteBuffer.getDouble();

                // 剩下的所有byte转换为string
                int pos = byteBuffer.position();
                String s = new String(buf, pos, byteCount - pos - 1);

                // 输出流
                OutputStream output = socket.getOutputStream();
                output.write(buf, 0, byteCount);

                input.close();
                output.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("客户端已退出：" + clientName);
            }
        }
    }

    /**
     * 这里主要演示不同的构造方法
     *
     * @return
     * @throws IOException
     */
    private static ServerSocket createServerSocket() throws IOException {
        // 创建基础的ServerSocket
        ServerSocket serverSocket = new ServerSocket();

        // 绑定到本地端口，并且指定accept等待队列为50
        // ServerSocket serverSocket = new ServerSocket(PORT);

        // 同上
        // ServerSocket serverSocket = new ServerSocket(PORT,50);

        // 绑定到指定ip上（针对多网卡）
        // ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getLocalHost());

        return serverSocket;
    }

    private static void initServerSocket(ServerSocket serverSocket) throws SocketException {
        // 是否复用未完全关闭的地址端口
        serverSocket.setReuseAddress(true);

        // 接收缓冲区大小，等效 Socket#setReceiveBufferSize
        serverSocket.setReceiveBufferSize(64 * 1024);

        // 设置ServerSocket#accept超时时间，一般不设置
        // serverSocket.setSoTimeout(2000);

        // 设置性能参数：短链接，延迟，带宽的相对重要性
        serverSocket.setPerformancePreferences(1, 1, 1);
    }
}
