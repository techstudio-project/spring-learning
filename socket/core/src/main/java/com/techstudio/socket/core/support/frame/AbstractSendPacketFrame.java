package com.techstudio.socket.core.support.frame;

import com.techstudio.socket.core.AbstractSendPacket;
import com.techstudio.socket.core.Frame;
import com.techstudio.socket.core.IOArgs;

import java.io.IOException;

public abstract class AbstractSendPacketFrame extends AbstractSendFrame {

    protected volatile AbstractSendPacket<?> sendPacket;

    public AbstractSendPacketFrame(int length, byte type, byte flag, short identifier,
                                   AbstractSendPacket sendPacket) {
        super(length, type, flag, identifier);
        this.sendPacket = sendPacket;
    }

    /**
     * 获取当前对应的发送Packet
     *
     * @return SendPacket
     */
    public synchronized AbstractSendPacket getSendPacket() {
        return sendPacket;
    }

    @Override
    public synchronized boolean handle(IOArgs args) throws IOException {
        if (sendPacket == null && !isSending()) {
            // 已取消，并且未发送任何数据，直接返回结束，发送下一帧
            return true;
        }
        return super.handle(args);
    }

    /**
     * 构建下一帧时做一次判断，如果已经终止，则没有下一帧；
     * 如果没有则尝试进行构建操作
     *
     * @return 下一帧
     */
    @Override
    public final synchronized Frame nextFrame() {
        return sendPacket == null ? null : buildNextFrame();
    }


    /**
     * 终止当前帧
     * 需要在当前方法中做一些操作，以及状态的维护
     * 后续可以扩展{@link #fillDirtyDataOnAbort()}方法对数据进行填充操作
     *
     * @return True：完美终止，可以顺利的移除当前帧；False：已发送部分数据
     */
    public final synchronized boolean abort() {
        // True, 当前帧没有发送任何数据
        // 1234, 12,34
        boolean isSending = isSending();
        if (isSending) {
            fillDirtyDataOnAbort();
        }

        sendPacket = null;

        return !isSending;
    }

    protected void fillDirtyDataOnAbort() {

    }

    /**
     * 构建下一帧
     *
     * @return NULL：没有下一帧
     */
    protected abstract Frame buildNextFrame();

}
