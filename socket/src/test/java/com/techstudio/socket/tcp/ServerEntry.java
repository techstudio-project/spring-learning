package com.techstudio.socket.tcp;

import com.techstudio.socket.nio.NIOTCPServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lj
 * @since 2020/3/31
 */
public class ServerEntry {

    public static void main(String[] args) throws IOException {

        // TCPServer tcpServer = new TCPServer(20000);
        TCPServer tcpServer = new NIOTCPServer(20000);
        tcpServer.start();

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        do {
            str = consoleReader.readLine();
            tcpServer.broadcast(str);
        } while (!"exit".equalsIgnoreCase(str));

        tcpServer.stop();
    }

}
