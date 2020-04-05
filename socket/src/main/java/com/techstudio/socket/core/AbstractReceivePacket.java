package com.techstudio.socket.core;

/**
 * 接收包
 *
 * @author lj
 * @since 2020/4/4
 */
public abstract class AbstractReceivePacket extends AbstractPacket {

    public AbstractReceivePacket(int length) {
        super(length);
    }

    public abstract void save(byte[] bytes, int count);

}
