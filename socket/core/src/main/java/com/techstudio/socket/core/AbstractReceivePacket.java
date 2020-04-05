package com.techstudio.socket.core;

import java.io.OutputStream;

/**
 * 接收包
 *
 * @author lj
 * @since 2020/4/4
 */
public abstract class AbstractReceivePacket<T extends OutputStream> extends AbstractPacket<T> {

    public AbstractReceivePacket(int length) {
        super(length);
    }

}
