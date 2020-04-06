package com.techstudio.socket.core.support.frame;

import com.techstudio.socket.core.Frame;
import com.techstudio.socket.core.IOArgs;

/**
 * 取消发送帧，用于标志某Packet取消进行发送数据
 */
public class CancelSendFrame extends AbstractSendFrame {

    public CancelSendFrame(short identifier) {
        super(0, Frame.TYPE_COMMAND_SEND_CANCEL, Frame.FLAG_NONE, identifier);
    }

    @Override
    protected int consumeBody(IOArgs args) {
        return 0;
    }

    @Override
    public Frame nextFrame() {
        return null;
    }
}
