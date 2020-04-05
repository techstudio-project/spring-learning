package com.techstudio.socket.core;

/**
 * 发送包的定义
 *
 * @author lj
 * @since 2020/4/4
 */
public abstract class AbstractSendPacket extends AbstractPacket {

    private boolean canceled;

    public AbstractSendPacket(int length) {
        super(length);
    }

    public abstract byte[] getBytes();

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
