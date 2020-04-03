package com.techstudio.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author lj
 * @since 2020/4/1
 */
public class SocketReadHandler extends Thread {

    protected SocketHandler socketHandler;
    protected boolean exit = false;
    private InputStream inputStream;

    public SocketReadHandler(SocketHandler socketHandler) throws IOException {
        init(socketHandler);
    }

    private void init(SocketHandler socketHandler) throws IOException {
        this.socketHandler = socketHandler;
        this.inputStream = socketHandler.getSocket().getInputStream();
    }

    @Override
    public void run() {
        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(inputStream));
            do {
                String str = bufReader.readLine();
                // 关闭流时返回null，这时客户端应该退出
                if (str == null) {
                    System.out.println("客户端已无法读取数据！");
                    socketHandler.exitBySelf();
                    break;
                }

                // 回调逻辑：交给调用者转发消息
                socketHandler.getSocketHandlerCallback().onMessageReceive(socketHandler, str);

            } while (!exit);

        } catch (IOException e) {
            if (!exit) {
                System.out.println(socketHandler.getClientInfo() + "：连接异常断开");
                socketHandler.exitBySelf();
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void exit() {
        exit = true;
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
