package com.techstudio.socket.server;

import com.techstudio.socket.server.nio.NIOTCPServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lj
 * @since 2020/4/5
 */
public class ServerApp {

    public static void main(String[] args) throws IOException {

        // TCPServer tcpServer = new TCPServer(20000);
        NIOTCPServer tcpServer = new NIOTCPServer(20000);
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
