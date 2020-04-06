package com.techstudio.socket.core;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 接收包
 *
 * @author lj
 * @since 2020/4/4
 */
public abstract class AbstractReceivePacket<S extends OutputStream, E> extends AbstractPacket<S> {

    // 定义当前接收包最终的实体
    private E entity;

    public AbstractReceivePacket(long length) {
        super(length);
    }

    /**
     * 得到最终接收到的数据实体
     *
     * @return 数据实体
     */
    public E getEntity() {
        return entity;
    }

    /**
     * 根据接收到的流转化为对应的实体
     *
     * @param stream {@link OutputStream}
     * @return 实体
     */
    protected abstract E buildEntity(S stream);

    /**
     * 先关闭流，随后将流的内容转化为对应的实体
     *
     * @param stream 待关闭的流
     * @throws IOException IO异常
     */
    @Override
    public final void closeStream(S stream) throws IOException {
        super.closeStream(stream);
        entity = buildEntity(stream);
    }

}
