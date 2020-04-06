package com.techstudio.socket.core;

import java.io.InputStream;

/**
 * 发送包的定义
 *
 * @author lj
 * @since 2020/4/4
 */
public abstract class AbstractSendPacket<S extends InputStream> extends AbstractPacket<S> {

    private boolean canceled;

    public AbstractSendPacket(long length) {
        super(length);
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void cancel(){
        setCanceled(true);
    }
}
