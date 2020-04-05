package com.techstudio.socket.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 发送包的定义
 *
 * @author lj
 * @since 2020/4/4
 */
public abstract class AbstractSendPacket<T extends InputStream> extends AbstractPacket<T> {

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
}
