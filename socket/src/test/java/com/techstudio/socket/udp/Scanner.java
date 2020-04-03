package com.techstudio.socket.udp;

import java.io.IOException;
import java.net.*;

/**
 * @author lj
 * @since 2020/3/28
 */
public class Scanner {

    public static void main(String[] args) throws IOException {

        DatagramSocket ds = new DatagramSocket(30000);

        // 接收数据线程
        ReceiveThread receiveThread = new ReceiveThread(ds);
        receiveThread.start();

        // 发送数据线程
        SendThread sendThread = new SendThread(ds);
        sendThread.start();


        System.in.read();
        receiveThread.exist();
        sendThread.exist();
    }

    public static class ReceiveThread extends Thread {

        private final DatagramSocket ds;
        private boolean exist = false;

        public ReceiveThread(DatagramSocket ds) {
            this.ds = ds;
        }


        @Override
        public void run() {
            System.out.println("Receive Thread started");
            try {
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

                }
            } catch (Exception e) {

            } finally {

            }
            System.out.println("Receive Thread stopped");
        }

        private void close() {
            if (ds != null) {
                ds.close();
            }
        }

        public void exist() {
            exist = true;
            close();
        }

    }

    public static class SendThread extends Thread {
        private final DatagramSocket ds;
        private boolean exist = false;

        public SendThread(DatagramSocket ds) {
            this.ds = ds;
        }

        @Override
        public void run() {
            System.out.println("Send Thread started");
            try {
                while (!exist) {
                    // 回送消息
                    String respStr = "hello this is scanner,收到请回复";
                    byte[] respStrBytes = respStr.getBytes();
                    // 构建发送数据报文实体
                    DatagramPacket respPacket = new DatagramPacket(respStrBytes, respStrBytes.length);
                    respPacket.setAddress(InetAddress.getLocalHost());
                    respPacket.setPort(20000);

                    ds.send(respPacket);

                    // 每隔三秒发送一次
                    Thread.sleep(3000);
                }
            } catch (Exception e) {

            } finally {
                close();
            }

            System.out.println("Send Thread stopped");
        }

        private void close() {
            if (ds != null) {
                ds.close();
            }
        }

        public void exist() {
            exist = true;
            close();
        }
    }

}
