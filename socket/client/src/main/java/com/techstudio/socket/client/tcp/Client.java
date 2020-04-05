package com.techstudio.socket.client.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 * @author lj
 * @since 2020/3/27
 */
public class Client {

    private static final int REMOTE_PORT = 8080;
    private static final int LOCAL_PORT = 9000;

    public void main(String[] args) throws IOException {
        Socket socket = createSocket();

        initSocket(socket);

        // 连接到远程服务器
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(),
                REMOTE_PORT), 3000);

        sendMessage1(socket);

        socket.close();
    }

    public static void sendMessage(Socket socket) throws IOException {
        // 构建键盘控制台输入流
        InputStream in = System.in;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        // 构建socket输出流，并转换为打印流
        OutputStream socketOut = socket.getOutputStream();
        PrintStream socketOutPrintStream = new PrintStream(socketOut);

        // 构建socket输入流，转换为BufferedReader
        InputStream socketIn = socket.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(socketIn));

        boolean exit = false;

        do {
            // 获得键盘输入
            String str = bufferedReader.readLine();

            // 通过打印输出流，发送给服务端
            socketOutPrintStream.println(str);

            // 接收服务端的返回
            String respStr = socketBufferedReader.readLine();
            if ("exit".equalsIgnoreCase(respStr)) {
                exit = true;
            } else {
                System.out.println("服务端返回：" + respStr);
            }

        } while (!exit);

        bufferedReader.close();
        socketOutPrintStream.close();
        socketBufferedReader.close();

    }

    public static void sendMessage1(Socket socket) throws IOException {
        OutputStream output = socket.getOutputStream();

        byte[] buf = new byte[1024];
        ByteBuffer byteBuffer = ByteBuffer.wrap(buf);

        byteBuffer.put((byte) 126);

        // char
        char c = 'a';
        byteBuffer.putChar(c);

        // int
        int i = 2323123;
        byteBuffer.putInt(i);

        // bool
        boolean b = true;
        byteBuffer.put(b ? (byte) 1 : (byte) 0);

        // Long
        long l = 298789739;
        byteBuffer.putLong(l);


        // float
        float f = 12.345f;
        byteBuffer.putFloat(f);


        // double
        double d = 13.31241248782973d;
        byteBuffer.putDouble(d);

        // String
        String str = "Hello你好！";
        byteBuffer.put(str.getBytes());

        // 发送到服务端
        output.write(buf, 0, byteBuffer.position() + 1);

        InputStream input = socket.getInputStream();

        byte[] respBuf = new byte[1024];
        int count = input.read(respBuf);
        System.out.println("服务器返回字节数：" + count);

        input.close();
        output.close();
    }

    private static Socket createSocket() throws IOException {
        // 等同于无参构造函数
        // Socket socket =new Socket(Proxy.NO_PROXY);

        // 新建一个具有http代理的套接字，数据传输将通过local.domain.com：9090 转发
        // Proxy proxy = new Proxy(Proxy.Type.HTTP,
        //        new InetSocketAddress(InetAddress.getByName("local.domain.com"), 9090));
        // Socket socket = new Socket(proxy);

        // 创建socket 直接连接到本机的8080端口
        // Socket socket = new Socket("localhost", REMOTE_PORT);

        // 效果同上
        // Socket socket = new Socket(InetAddress.getLocalHost(), REMOTE_PORT);

        // 创建socket 直接连接到本机的8080端口,并且绑定到本机的9000端口
        // Socket socket = new Socket("localhost",REMOTE_PORT,InetAddress.getLocalHost(),LOCAL_PORT);

        // 效果同上
        // Socket socket = new Socket(InetAddress.getLocalHost(),REMOTE_PORT,InetAddress.getLocalHost(),LOCAL_PORT);

        Socket socket = new Socket();
        socket.bind(new InetSocketAddress(InetAddress.getLocalHost(), LOCAL_PORT));
        return socket;
    }

    private static void initSocket(Socket socket) throws SocketException {
        // 设置读取超时时间
        socket.setSoTimeout(2000);

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
