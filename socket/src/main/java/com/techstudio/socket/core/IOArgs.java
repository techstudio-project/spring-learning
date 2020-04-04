package com.techstudio.socket.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * io输入输出参数封装
 *
 * @author lj
 * @since 2020/4/4
 */
public class IOArgs {

    private byte[] bytes = new byte[256];
    private ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

    public int read(SocketChannel channel) throws IOException {
        byteBuffer.clear();
        return channel.read(byteBuffer);
    }

    public int write(SocketChannel channel) throws IOException {
        return channel.write(byteBuffer);
    }

    public String buffer2String() {
        // 丢弃最后换行符
        return new String(bytes, 0, byteBuffer.position());
    }

    public interface IOArgsEventListener {

        void onStarted(IOArgs args);

        void onCompleted(IOArgs args);
    }
}
