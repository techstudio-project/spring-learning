package com.techstudio.socket.core;

import java.io.Closeable;

/**
 * @author lj
 * @since 2020/4/4
 */
public abstract class AbstractPacket implements Closeable {

    // BYTES 类型
    public static final byte TYPE_MEMORY_BYTES = 1;
    // String 类型
    public static final byte TYPE_MEMORY_STRING = 2;
    // 文件 类型
    public static final byte TYPE_STREAM_FILE = 3;
    // 长链接流 类型
    public static final byte TYPE_STREAM_DIRECT = 4;

    private final int length;

    public AbstractPacket(int length) {
        this.length = length;
    }

    public abstract byte getType();

    public int getLength() {
        return length;
    }
}
