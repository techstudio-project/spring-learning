package com.techstudio.socket.core.support.frame;

import com.techstudio.socket.core.AbstractSendPacket;
import com.techstudio.socket.core.Frame;
import com.techstudio.socket.core.IOArgs;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

public class SendEntityFrame extends AbstractSendPacketFrame {

    private final ReadableByteChannel readableByteChannel;
    private final long unConsumeEntityLength;

    public SendEntityFrame(short identifier, long entityLength, ReadableByteChannel readableByteChannel,
                           AbstractSendPacket packet) {
        super((int) Math.min(entityLength, Frame.MAX_CAPACITY),
                Frame.TYPE_PACKET_ENTITY,
                Frame.FLAG_NONE,
                identifier,
                packet);
        // body中还可容纳量即为可消费长度，剩下的就是没有消费的长度
        this.unConsumeEntityLength = entityLength - bodyRemaining;
        this.readableByteChannel = readableByteChannel;
    }

    @Override
    protected int consumeBody(IOArgs args) throws IOException {
        if (sendPacket == null) {
            // 已终止当前帧，则填充假数据
            return args.fillEmpty(bodyRemaining);
        }
        return args.readFrom(readableByteChannel);
    }

    @Override
    public Frame buildNextFrame() {
        if (unConsumeEntityLength == 0) {
            return null;
        }
        // 将未消费的长度用于构建下一帧
        return new SendEntityFrame(getBodyIdentifier(),
                unConsumeEntityLength, readableByteChannel, sendPacket);
    }
}
