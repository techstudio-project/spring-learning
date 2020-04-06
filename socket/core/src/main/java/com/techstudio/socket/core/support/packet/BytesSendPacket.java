package com.techstudio.socket.core.support.packet;

import com.techstudio.socket.core.AbstractSendPacket;

import java.io.ByteArrayInputStream;

/**
 * @author lj
 * @since 2020/4/5
 */
public class BytesSendPacket extends AbstractSendPacket<ByteArrayInputStream> {

    private final byte[] bytes;

    public BytesSendPacket(byte[] bytes) {
        super(bytes.length);
        this.bytes = bytes;
    }

    @Override
    public byte getType() {
        return TYPE_MEMORY_BYTES;
    }

    @Override
    public ByteArrayInputStream createStream() {
        return new ByteArrayInputStream(bytes);
    }
}
