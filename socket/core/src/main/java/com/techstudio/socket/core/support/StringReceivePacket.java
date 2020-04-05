package com.techstudio.socket.core.support;

import com.techstudio.socket.core.AbstractReceivePacket;

import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public class StringReceivePacket extends AbstractReceivePacket {

    private byte[] buffer;
    private int position = 0;

    public StringReceivePacket(int length) {
        super(length);
        this.buffer = new byte[length];
    }

    @Override
    public byte getType() {
        return TYPE_MEMORY_STRING;
    }

    @Override
    public void save(byte[] bytes, int count) {
        System.arraycopy(bytes, 0, buffer, position, count);
        position += count;
    }


    public String string(){
        return new String(buffer);
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void close() throws IOException {

    }
}
