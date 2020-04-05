package com.techstudio.socket.core.support;

import com.techstudio.socket.core.AbstractSendPacket;

import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public class StringSendPacket extends AbstractSendPacket {

    private final byte[] bytes;

    public StringSendPacket(String msg) {
        super(msg.length());
        this.bytes = msg.getBytes();
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public byte getType() {
        return TYPE_STREAM_FILE;
    }

    @Override
    public void close() throws IOException {

    }
}
