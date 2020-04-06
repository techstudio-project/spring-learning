package com.techstudio.socket.core.support.frame;

import com.techstudio.socket.core.Frame;
import com.techstudio.socket.core.IOArgs;

import java.io.IOException;

public abstract class AbstractSendFrame extends Frame {
    // 帧头可读写剩余区域大小 ，初始值为header的总长度，保证多线程可见性
    protected volatile byte headerRemaining = Frame.FRAME_HEADER_LENGTH;
    // 帧体可读写剩余区域大小，保证多线程可见性
    protected volatile int bodyRemaining;

    public AbstractSendFrame(int length, byte type, byte flag, short identifier) {
        super(length, type, flag, identifier);
        bodyRemaining = length;
    }

    @Override
    public synchronized boolean handle(IOArgs args) throws IOException {
        try {
            args.setLimit(headerRemaining + bodyRemaining);
            args.startWriting();

            if (headerRemaining > 0 && args.remained()) {
                headerRemaining -= consumeHeader(args);
            }

            if (headerRemaining == 0 && args.remained() && bodyRemaining > 0) {
                bodyRemaining -= consumeBody(args);
            }

            return headerRemaining == 0 && bodyRemaining == 0;
        } finally {
            args.finishWriting();
        }
    }

    @Override
    public int getConsumableLength() {
        return headerRemaining + bodyRemaining;
    }


    protected abstract int consumeBody(IOArgs args) throws IOException;

    /**
     * 是否已经处于发送数据中，如果已经发送了部分数据则返回True
     * 只要头部数据已经开始消费，则肯定已经处于发送数据中
     *
     * @return True，已发送部分数据
     */
    protected synchronized boolean isSending() {
        return headerRemaining < Frame.FRAME_HEADER_LENGTH;
    }

    private byte consumeHeader(IOArgs args) {
        int count = headerRemaining;
        int offset = header.length - count;
        return (byte) args.readFrom(header, offset, count);
    }
}
