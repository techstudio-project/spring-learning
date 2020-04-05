package com.techstudio.socket.core.support.packet;

import com.techstudio.socket.core.AbstractReceivePacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public class StringReceivePacket extends AbstractReceivePacket<ByteArrayOutputStream> {

    private String str;

    public StringReceivePacket(int length) {
        super(length);
    }

    @Override
    public byte getType() {
        return TYPE_MEMORY_STRING;
    }

    @Override
    public ByteArrayOutputStream createStream() {
        return new ByteArrayOutputStream((int) getLength());
    }

    @Override
    public void closeStream(ByteArrayOutputStream stream) throws IOException {
        str = new String(stream.toByteArray());
        super.closeStream(stream);
    }

    public String string() {
        return str;
    }


}
