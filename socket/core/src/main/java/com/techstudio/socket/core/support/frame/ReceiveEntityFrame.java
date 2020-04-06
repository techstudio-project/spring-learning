package com.techstudio.socket.core.support.frame;

import com.techstudio.socket.core.IOArgs;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public class ReceiveEntityFrame extends AbstractReceiveFrame {
    private WritableByteChannel channel;

    ReceiveEntityFrame(byte[] header) {
        super(header);
    }

    public void bindPacketChannel(WritableByteChannel channel) {
        this.channel = channel;
    }

    @Override
    protected int consumeBody(IOArgs args) throws IOException {
        return channel == null ? args.setEmpty(bodyRemaining)
                : args.writeTo(channel);
    }
}
