package com.techstudio.socket.core.support.packet;

import com.techstudio.socket.core.AbstractSendPacket;

import java.io.ByteArrayInputStream;

/**
 * @author lj
 * @since 2020/4/4
 */
public class StringSendPacket extends AbstractSendPacket<ByteArrayInputStream> {

    private final byte[] bytes;

    public StringSendPacket(String msg) {
        super(msg.length());
        this.bytes = msg.getBytes();
    }

    @Override
    public byte getType() {
        return TYPE_STREAM_FILE;
    }

    @Override
    public ByteArrayInputStream createStream() {
        return new ByteArrayInputStream(bytes);
    }
}
