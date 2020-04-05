package com.techstudio.socket.core.support;

import com.techstudio.socket.core.IOArgs;
import com.techstudio.socket.core.IOProvider;
import com.techstudio.socket.core.Receiver;
import com.techstudio.socket.core.Sender;
import com.techstudio.socket.util.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lj
 * @since 2020/4/4
 */
public class SocketChannelAdapter implements Sender, Receiver, Closeable {

    private static final Logger logger = LoggerFactory.getLogger(SocketChannelAdapter.class);
    /**
     * 使用原子类保证多线程同时访问共享变量的安全性
     */
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final SocketChannel channel;
    private final IOProvider ioProvider;
    private final ChannelStatusChangedListener listener;

    private IOArgs.IOArgsEventListener sendEventListener;
    private IOArgs.IOArgsEventListener receiveEventListener;

    private IOArgs receiveIoArgsTemp;

    public SocketChannelAdapter(SocketChannel channel, IOProvider ioProvider, ChannelStatusChangedListener listener)
            throws IOException {
        this.channel = channel;
        this.ioProvider = ioProvider;
        this.listener = listener;
        channel.configureBlocking(false);
    }

    @Override
    public void setReceiveListener(IOArgs.IOArgsEventListener listener) {
        this.receiveEventListener = listener;
    }

    @Override
    public boolean receiveAsync(IOArgs ioArgs) throws IOException {
        if (isClosed.get()) {
            throw new IOException("socket channel is closed");
        }
        receiveIoArgsTemp = ioArgs;
        return ioProvider.registerInput(channel, inputCallback);
    }

    @Override
    public boolean sendAsync(IOArgs args, IOArgs.IOArgsEventListener listener) throws IOException {
        if (isClosed.get()) {
            throw new IOException("socket channel is closed");
        }
        this.sendEventListener = listener;
        // 把发送的数据附加到回调中
        outputCallback.setAttach(args);
        return ioProvider.registerOutput(channel, outputCallback);
    }

    @Override
    public void close() throws IOException {
        // 比较替换操作，保证线程安全
        if (isClosed.compareAndSet(false, true)) {
            ioProvider.unRegisterInput(channel);
            ioProvider.unRegisterOutput(channel);
            CloseableUtils.close(logger, channel);
            // 回调，告诉调用者channel已经关闭了
            listener.onStatusChanged(channel);
        }
    }

    private final IOProvider.HandleInputCallback inputCallback = new IOProvider.HandleInputCallback() {
        @Override
        protected void canProvideInput() {
            if (isClosed.get()) {
                return;
            }
            // 读取开始的回调
            receiveEventListener.onStarted(receiveIoArgsTemp);
            try {
                // 开始从channel读取到buffer
                if (receiveIoArgsTemp.readFrom(channel) > 0) {
                    // 读取完成时的回调
                    receiveEventListener.onCompleted(receiveIoArgsTemp);
                } else {
                    throw new IOException("无法从socketChannel读取数据");
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                CloseableUtils.close(logger, SocketChannelAdapter.this);
            }
        }
    };

    private final IOProvider.HandleOutputCallback outputCallback = new IOProvider.HandleOutputCallback() {
        @Override
        protected void canProvideOutput(Object attach) {
            if (isClosed.get()) {
                return;
            }
            IOArgs ioArgs = (IOArgs) getAttach();

            sendEventListener.onStarted(ioArgs);
            try {
                // 开始从channel读取到buffer
                if (ioArgs.writeTo(channel) > 0) {
                    // 读取完成时的回调
                    sendEventListener.onCompleted(ioArgs);
                } else {
                    throw new IOException("无法写入数据到socketChannel");
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                CloseableUtils.close(logger, SocketChannelAdapter.this);
            }
        }
    };

    public interface ChannelStatusChangedListener {
        void onStatusChanged(SocketChannel channel);
    }
}
