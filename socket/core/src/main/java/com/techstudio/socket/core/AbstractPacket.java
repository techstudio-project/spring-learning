package com.techstudio.socket.core;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public abstract class AbstractPacket<S extends Closeable> implements Closeable {

    // BYTES 类型
    public static final byte TYPE_MEMORY_BYTES = 1;
    // String 类型
    public static final byte TYPE_MEMORY_STRING = 2;
    // 文件 类型
    public static final byte TYPE_STREAM_FILE = 3;
    // 长链接流 类型
    public static final byte TYPE_STREAM_DIRECT = 4;

    private S stream;
    private final long length;

    public AbstractPacket(long length) {
        this.length = length;
    }

    public abstract byte getType();


    public abstract S createStream();

    public void closeStream(S stream) throws IOException {
        stream.close();
    }

    public final S open() {
        if (stream == null) {
            stream = createStream();
        }
        return stream;
    }

    @Override
    public final void close() throws IOException {
        if (stream != null) {
            closeStream(stream);
            stream = null;
        }
    }

    public long getLength() {
        return length;
    }

    /**
     * 头部额外信息，用于携带额外的校验信息等
     *
     * @return byte 数组，最大255长度
     */
    public byte[] headerInfo() {
        return null;
    }

}
