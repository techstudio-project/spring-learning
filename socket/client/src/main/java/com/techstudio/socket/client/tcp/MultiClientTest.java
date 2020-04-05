package com.techstudio.socket.client.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lj
 * @since 2020/4/2
 */
public class MultiClientTest {

    public void main(String[] args) {

        List<TCPClient> tcpClients = new ArrayList<>(1000);
        TCPClient tcpClient;

        // 建立1000个客户端连接
        for (int i = 0; i < 1000; i++) {
            try {
                tcpClient = new TCPClient(20000);
                tcpClient.start();
                tcpClients.add(tcpClient);
                Thread.sleep(20);
                System.out.println("连接成功client id：" + i);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                continue;
            }

        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        do {
            try {
                str = reader.readLine();
                for (TCPClient client : tcpClients) {
                    client.send(str);
                    Thread.sleep(20);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        } while (!"exit".equalsIgnoreCase(str));
    }
}
