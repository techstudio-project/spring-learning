package com.techstudio.socket.server.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lj
 * @since 2020/4/1
 */
public class SocketWriteHandler {

    private SocketHandler socketHandler;
    private OutputStream outputStream;
    private PrintStream printStream;
    protected boolean exit = false;

    // 通过异步线程池发送消息
    protected ExecutorService executorService = Executors.newSingleThreadExecutor();

    public SocketWriteHandler(SocketHandler socketHandler) throws IOException {
        init(socketHandler);
    }

    private void init(SocketHandler socketHandler) throws IOException {
        this.socketHandler = socketHandler;
        this.outputStream = socketHandler.getSocket().getOutputStream();
        this.printStream = new PrintStream(outputStream);
    }


    public void exit() {
        exit = true;
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdownNow();
    }

    public void send(String msg) {
        executorService.execute(new WriteRunnable(msg));
    }

    private class WriteRunnable implements Runnable {

        private final String msg;

        private WriteRunnable(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            if (SocketWriteHandler.this.exit) {
                return;
            }
            SocketWriteHandler.this.printStream.println(msg);
        }
    }
}
