package com.techstudio.socket.nio;

import com.techstudio.socket.tcp.SocketHandler;
import com.techstudio.socket.tcp.SocketWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author lj
 * @since 2020/4/3
 */
public class NIOSocketWriteHandler extends SocketWriteHandler {

    private static final Logger logger = LoggerFactory.getLogger(NIOSocketWriteHandler.class);
    private final Selector writeSelector;
    private final SocketChannel channel;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(512);

    public NIOSocketWriteHandler(SocketHandler socketHandler) throws IOException {
        super(socketHandler);
        throw new UnsupportedOperationException();
    }

    public NIOSocketWriteHandler(SocketHandler socketHandler, Selector writeSelector, SocketChannel channel) throws IOException {
        super(socketHandler);
        this.writeSelector = writeSelector;
        this.channel = channel;
    }

    @Override
    public void exit() {
        super.exit();
        try {
            writeSelector.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void send(String msg) {
        executorService.execute(new WriteRunnable(msg));
    }

    private class WriteRunnable implements Runnable {

        private final String msg;

        private WriteRunnable(String msg) {
            if (!msg.endsWith("\n")) {
                this.msg = msg + "\n";
            } else {
                this.msg = msg;
            }
        }

        @Override
        public void run() {
            if (exit) {
                return;
            }
            byteBuffer.clear();
            byteBuffer.put(msg.getBytes());
            // 反转操作，将指针回到0位置，为下面write做准备
            byteBuffer.flip();
            while (!exit && byteBuffer.hasRemaining()) {
                try {
                    // 简化了实现，并没有通过selector去选择需要发送数据的channel
                    int len = channel.write(byteBuffer);

                    if (len == 0) {
                        logger.error("客户端已无法发送数据！");
                        break;
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
