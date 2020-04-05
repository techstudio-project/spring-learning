package com.techstudio.socket.core;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * io输入输出参数封装
 *
 * @author lj
 * @since 2020/4/4
 */
public class IOArgs {

    // 默认和byteBuffer的容量一致
    private int limit = 256;
    private byte[] bytes = new byte[256];
    private ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

    /**
     * 读取bytes到ByteBuffer，返回总共读取字符的总数
     *
     * @param bytes  需要读取的byte数组
     * @param offset 位移
     * @return int
     */
    public int readFrom(byte[] bytes, int offset) {
        int size = Math.min(bytes.length - offset, byteBuffer.remaining());
        byteBuffer.put(bytes, offset, size);
        return size;
    }

    /**
     * 读取ByteBuffer到byte数组
     *
     * @param bytes
     * @param offset
     * @return
     */
    public int writeTo(byte[] bytes, int offset) {
        int size = Math.min(bytes.length - offset, byteBuffer.remaining());
        byteBuffer.get(bytes, offset, size);
        return size;
    }

    /**
     * 从SocketChannel读取数据到byteBuffer
     *
     * @param channel
     * @return
     * @throws IOException
     */
    public int readFrom(SocketChannel channel) throws IOException {
        startWriting();
        int byteProduced = 0;
        while (byteBuffer.hasRemaining()) {
            int len = channel.read(byteBuffer);
            if (len < 0) {
                throw new EOFException();
            }
            byteProduced += len;
        }
        finishWriting();
        return byteProduced;
    }

    /**
     * 将byteBuffer写入到SocketChannel
     *
     * @param channel
     * @return
     * @throws IOException
     */
    public int writeTo(SocketChannel channel) throws IOException {
        int byteProduced = 0;
        while (byteBuffer.hasRemaining()) {
            int len = channel.write(byteBuffer);
            if (len < 0) {
                throw new EOFException();
            }
            byteProduced += len;
        }
        return byteProduced;
    }

    /**
     * 对byteBuffer开始写之前的操作
     */
    public void startWriting() {
        // 回到初始状态
        byteBuffer.clear();

        byteBuffer.limit(getLimit());
    }

    /**
     * 对byteBuffer写完之后的操作
     */
    public void finishWriting() {
        // 反转
        byteBuffer.flip();
    }

    /**
     * 将packet的长度写到byteBuffer的首部
     *
     * @param total
     */
    public void writePacketLength(int total) {
        byteBuffer.putInt(total);
    }

    /**
     * 从byteBuffer的首部读取packet的长度
     *
     * @return
     */
    public int readPacketLength() {
        return byteBuffer.getInt();
    }

    public int getLimit() {
        return limit;
    }

    public int getCapacity(){
        return byteBuffer.capacity();
    }

    /**
     * 设置单次写操作的容纳取件，默认大小为byteBuffer的最大容量
     *
     * @param limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public interface IOArgsEventListener {

        void onStarted(IOArgs args);

        void onCompleted(IOArgs args);
    }
}
