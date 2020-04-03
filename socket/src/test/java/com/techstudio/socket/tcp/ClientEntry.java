package com.techstudio.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lj
 * @since 2020/3/31
 */
public class ClientEntry {

    public static void main(String[] args) throws IOException {
        TCPClient tcpClient = new TCPClient(20000);
        tcpClient.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        do {
            str = reader.readLine();
            tcpClient.send(str);
        } while (!"exit".equalsIgnoreCase(str));

        tcpClient.close();
    }

}