package com.techstudio.socket.client;

import com.techstudio.socket.client.nio.NIOTCPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lj
 * @since 2020/4/5
 */
public class ClientApp {

    public static void main(String[] args) throws IOException {
        // TCPClient tcpClient = new TCPClient(20000);
        // tcpClient.start();

        NIOTCPClient tcpClient = new NIOTCPClient(20000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        do {
            str = reader.readLine();
            tcpClient.send(str);
        } while (!"exit".equalsIgnoreCase(str));

        tcpClient.close();
    }

}
