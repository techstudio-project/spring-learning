package com.techstudio.socket.core;

import java.io.Closeable;

/**
 * 发送数据的调度者
 * 缓存所有需要发送的数据，通过队列对数据进行发送，并且在发送数据时实现对数据的包装
 *
 * @author lj
 * @since 2020/4/4
 */
public interface SendDispatcher extends Closeable {

    void send(AbstractSendPacket sendPacket);

    void cancel(AbstractSendPacket sendPacket);
}
