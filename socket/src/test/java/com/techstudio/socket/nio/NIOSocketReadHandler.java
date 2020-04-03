package com.techstudio.socket.nio;

import com.techstudio.socket.tcp.SocketHandler;
import com.techstudio.socket.tcp.SocketReadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author lj
 * @since 2020/4/3
 */
public class NIOSocketReadHandler extends SocketReadHandler {

    private static final Logger logger = LoggerFactory.getLogger(NIOSocketReadHandler.class);
    private final Selector readSelector;
    // 分配512字节的buffer
    private ByteBuffer byteBuffer = ByteBuffer.allocate(512);

    public NIOSocketReadHandler(SocketHandler socketHandler) throws IOException {
        super(socketHandler);
        throw new UnsupportedOperationException();
    }

    public NIOSocketReadHandler(SocketHandler socketHandler, Selector readSelector) throws IOException {
        super(socketHandler);
        this.readSelector = readSelector;
    }

    @Override
    public void run() {
        try {
            do {
                // 阻塞，选择可读的
                if (readSelector.select() == 0) {
                    if (exit) {
                        break;
                    }
                    continue;
                }
                Iterator<SelectionKey> iterator = readSelector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 先清空后在读取到buffer
                        byteBuffer.clear();
                        int count = channel.read(byteBuffer);
                        if (count > 0) {
                            // 去掉换行符，丢弃最后一个换行符
                            String str = new String(byteBuffer.array(), 0, byteBuffer.position());
                            // 回调逻辑：交给调用者转发消息
                            socketHandler.getSocketHandlerCallback().onMessageReceive(socketHandler, str);
                        }
                    }
                }
            } while (!exit);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                readSelector.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void exit() {
        exit = true;
        // 唤醒，取消阻塞，run 里面已经有关闭逻辑了
        readSelector.wakeup();
    }
}
