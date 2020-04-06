package com.techstudio.socket.core.support.packet;

import java.io.ByteArrayOutputStream;

/**
 * @author lj
 * @since 2020/4/4
 */
public class StringReceivePacket extends AbstractByteArrayReceivePacket<String> {

    public StringReceivePacket(long length) {
        super(length);
    }

    @Override
    protected String buildEntity(ByteArrayOutputStream stream) {
        return new String(stream.toByteArray());
    }

    @Override
    public byte getType() {
        return TYPE_MEMORY_STRING;
    }

}
