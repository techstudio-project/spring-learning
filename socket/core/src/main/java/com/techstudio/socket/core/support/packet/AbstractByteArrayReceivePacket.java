package com.techstudio.socket.core.support.packet;

import com.techstudio.socket.core.AbstractReceivePacket;

import java.io.ByteArrayOutputStream;

/**
 * @author lj
 * @since 2020/4/5
 */
public abstract class AbstractByteArrayReceivePacket<E> extends AbstractReceivePacket<ByteArrayOutputStream, E> {

    public AbstractByteArrayReceivePacket(long length) {
        super(length);
    }

    @Override
    public ByteArrayOutputStream createStream() {
        return new ByteArrayOutputStream((int) getLength());
    }
}
