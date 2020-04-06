package com.techstudio.socket.core;

import java.io.Closeable;

/**
 * 接收数据调度封装
 * 把多份（一份）ioArgs组合成一个packet
 *
 * @author lj
 * @since 2020/4/4
 */
public interface ReceiveDispatcher extends Closeable {

    void start();

    void stop();

    interface ReceivePacketCallback {

        AbstractReceivePacket<?, ?> onArrivedNewPacket(byte type, long length);

        void onReceivePacketCompleted(AbstractReceivePacket receivePacket);
    }
}
