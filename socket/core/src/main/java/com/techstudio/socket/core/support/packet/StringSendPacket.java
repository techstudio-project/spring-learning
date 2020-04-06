package com.techstudio.socket.core.support.packet;

/**
 * @author lj
 * @since 2020/4/4
 */
public class StringSendPacket extends BytesSendPacket {

    public StringSendPacket(String msg) {
        super(msg.getBytes());
    }

    @Override
    public byte getType() {
        return TYPE_MEMORY_STRING;
    }
}
