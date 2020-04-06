package com.techstudio.socket.core.support.frame;

import com.techstudio.socket.core.AbstractSendPacket;
import com.techstudio.socket.core.Frame;
import com.techstudio.socket.core.IOArgs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Packet头帧
 */
public class SendHeaderFrame extends AbstractSendPacketFrame {

    public static final int PACKET_HEADER_FRAME_MIN_LENGTH = 6;
    private final byte[] body;

    public SendHeaderFrame(short identifier, AbstractSendPacket sendPacket) {
        super(PACKET_HEADER_FRAME_MIN_LENGTH,
                Frame.TYPE_PACKET_HEADER,
                Frame.FLAG_NONE,
                identifier,
                sendPacket);

        final long packetLength = sendPacket.getLength();
        final byte packetType = sendPacket.getType();
        final byte[] packetHeaderInfo = sendPacket.headerInfo();

        // 头部对应的数据信息长度
        body = new byte[bodyRemaining];

        // 头5字节存储长度信息低5字节（40位）数据
        // 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
        body[0] = (byte) (packetLength >> 32);
        body[1] = (byte) (packetLength >> 24);
        body[2] = (byte) (packetLength >> 16);
        body[3] = (byte) (packetLength >> 8);
        body[4] = (byte) (packetLength);

        body[5] = packetType;

        if (packetHeaderInfo != null) {
            System.arraycopy(packetHeaderInfo, 0, body,
                    PACKET_HEADER_FRAME_MIN_LENGTH, packetHeaderInfo.length);
        }
    }

    @Override
    protected int consumeBody(IOArgs args) throws IOException {
        int count = bodyRemaining;
        int offset = body.length - count;
        return args.readFrom(body, offset, count);
    }

    @Override
    public Frame buildNextFrame() {
        InputStream stream = sendPacket.open();
        ReadableByteChannel channel = Channels.newChannel(stream);
        return new SendEntityFrame(getBodyIdentifier(), sendPacket.getLength(), channel, sendPacket);
    }
}
