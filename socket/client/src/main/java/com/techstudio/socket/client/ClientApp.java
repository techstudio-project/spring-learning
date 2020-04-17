package com.techstudio.socket.client;

import com.techstudio.socket.client.nio.NIOTCPClient;
import com.techstudio.socket.core.support.packet.FileSendPacket;

import java.io.BufferedReader;
import java.io.File;
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

        NIOTCPClient tcpClient = new NIOTCPClient(40000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        do {

            str = reader.readLine();
            if (str == null || str.length() == 0) {
                break;
            }

            if ("exit".equalsIgnoreCase(str)) {
                break;
            }
            // --f url
            if (str.startsWith("--f")) {
                String[] array = str.split(" ");
                if (array.length >= 2) {
                    String filePath = array[1];
                    File file = new File(filePath);
                    if (file.exists() && file.isFile()) {
                        FileSendPacket packet = new FileSendPacket(file);
                        tcpClient.send(packet);
                        continue;
                    }
                }
            }

            tcpClient.send(str);
        } while (true);

        tcpClient.close();
    }

}
