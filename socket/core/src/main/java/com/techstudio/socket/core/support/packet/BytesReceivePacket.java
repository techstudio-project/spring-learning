package com.techstudio.socket.core.support.packet;

import java.io.ByteArrayOutputStream;

/**
 * @author lj
 * @since 2020/4/5
 */
public class BytesReceivePacket extends AbstractByteArrayReceivePacket<byte[]> {

    public BytesReceivePacket(long length) {
        super(length);
    }

    @Override
    protected byte[] buildEntity(ByteArrayOutputStream stream) {
        return stream.toByteArray();
    }

    @Override
    public byte getType() {
        return TYPE_MEMORY_BYTES;
    }
}
