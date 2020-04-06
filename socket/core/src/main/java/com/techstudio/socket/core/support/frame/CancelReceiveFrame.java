package com.techstudio.socket.core.support.frame;

import com.techstudio.socket.core.IOArgs;

import java.io.IOException;

/**
 * 取消传输帧，接收实现
 */
public class CancelReceiveFrame extends AbstractReceiveFrame {

    CancelReceiveFrame(byte[] header) {
        super(header);
    }

    @Override
    protected int consumeBody(IOArgs args) throws IOException {
        return 0;
    }
}
