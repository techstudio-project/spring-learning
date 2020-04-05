package com.techstudio.socket.client.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author lj
 * @since 2020/4/2
 */
public class TCPClient {

    private Socket socket;
    private ReadHandler readHandler;
    private PrintStream printStream;

    public TCPClient(int remotePort) throws IOException {
        this(remotePort, null);
    }

    public TCPClient(int remotePort, Integer localPort) throws IOException {
        init(remotePort, localPort);
    }

    public void start() throws IOException {
        if (readHandler == null) {
            readHandler = new ReadHandler(socket.getInputStream());
        }
        if(printStream ==null){
            printStream = new PrintStream(socket.getOutputStream());
        }
        readHandler.start();
    }

    public void close() {
        readHandler.exit();
        printStream.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        printStream.println(msg);
    }

    private void init(int remotePort, Integer localPort) throws IOException {
        Socket socket = new Socket();
        if (localPort != null) {
            socket.bind(new InetSocketAddress(InetAddress.getLocalHost(), localPort));
        }
        initSocket(socket);
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), remotePort), 3000);
        this.socket = socket;
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

    private class ReadHandler extends Thread {
        private boolean exit = false;
        private final InputStream inputStream;

        public ReadHandler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                do {
                    String str = bufferedReader.readLine();
                    if (str == null) {
                        System.out.println("连接已关闭，无法读取数据！");
                        break;
                    }
                    System.out.println(str);
                } while (!exit);
            } catch (Exception e) {
                if (!exit) {
                    System.out.println("连接异常断开：" + e.getMessage());
                }
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void exit() {
            exit = true;
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
