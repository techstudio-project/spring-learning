package com.techstudio.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author lj
 * @since 2020/3/28
 */
public class Provider {

    public static void main(String[] args) throws IOException {

        ListenThread listenThread = new ListenThread();
        listenThread.start();

        // 读取控制台任意字符，进行退出
        System.in.read();

        // 退出监听线程
        listenThread.exist();
    }

    public static class ListenThread extends Thread {

        private boolean exist = false;

        private DatagramSocket ds = null;

        @Override
        public void run() {

            System.out.println("provider listen started");

            try {
                // 监听20000端口
                ds = new DatagramSocket(20000);
                while (!exist) {

                    // 构建接收实体
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

                    // 接收 (阻塞线程，等待有消息发送过来)
                    ds.receive(receivePacket);

                    // 打印收到的信息
                    String ip = receivePacket.getAddress().getHostAddress();
                    int port = receivePacket.getPort();
                    int len = receivePacket.getLength();
                    String dataStr = new String(receivePacket.getData(), 0, len);
                    System.out.println("收到消息：ip=" + ip + ",port=" + port + ",data=" + dataStr);

                    // 发送消息
                    String respStr = "收到你发来的消息，len=" + len;
                    byte[] respStrBytes = respStr.getBytes();
                    // 构建发送数据报文实体
                    DatagramPacket respPacket = new DatagramPacket(respStrBytes, respStrBytes.length,
                            receivePacket.getAddress(), receivePacket.getPort());

                     ds.send(respPacket);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }

            System.out.println("provider listen stopped");
        }

        public void exist() {
            exist = true;
            close();
        }

        private void close() {
            if (ds != null) {
                ds.close();
            }
        }
    }

}
