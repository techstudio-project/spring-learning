package com.techstudio.socket.core.support;

import com.techstudio.socket.core.IOArgs;
import com.techstudio.socket.core.IOProvider;
import com.techstudio.socket.core.Receiver;
import com.techstudio.socket.core.Sender;
import com.techstudio.socket.core.util.CloseableUtils;
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

    private IOArgs.IOArgsEventProcessor sendEventProcessor;
    private IOArgs.IOArgsEventProcessor receiveEventProcessor;

    public SocketChannelAdapter(SocketChannel channel, IOProvider ioProvider,
                                ChannelStatusChangedListener listener)
            throws IOException {
        this.channel = channel;
        this.ioProvider = ioProvider;
        this.listener = listener;
        channel.configureBlocking(false);
    }

    @Override
    public void close() {
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
            IOArgs args = receiveEventProcessor.provideIoArgs();
            try {
                if (args == null) {
                    receiveEventProcessor.onConsumeFailed(null, new IOException("IOArgs is null"));
                }
                // 开始从channel读取到buffer
                else if (args.readFrom(channel) > 0) {
                    // 读取完成时的回调
                    receiveEventProcessor.onConsumeCompleted(args);
                } else {
                    receiveEventProcessor.onConsumeFailed(args, new IOException("无法从socketChannel读取数据"));
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                CloseableUtils.close(logger, SocketChannelAdapter.this);
            }
        }
    };

    private final IOProvider.HandleOutputCallback outputCallback = new IOProvider.HandleOutputCallback() {
        @Override
        protected void canProvideOutput() {
            if (isClosed.get()) {
                return;
            }
            IOArgs args = sendEventProcessor.provideIoArgs();
            try {
                if (args == null) {
                    sendEventProcessor.onConsumeFailed(null, new IOException("IOArgs is null"));
                }
                // 将数据写入到socketChannel
                else if (args.writeTo(channel) > 0) {
                    // 读取完成时的回调
                    sendEventProcessor.onConsumeCompleted(args);
                } else {
                    sendEventProcessor.onConsumeFailed(args, new IOException("无法写入数据到socketChannel"));
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                CloseableUtils.close(logger, SocketChannelAdapter.this);
            }
        }
    };

    @Override
    public void setReceiveListener(IOArgs.IOArgsEventProcessor processor) {
        this.receiveEventProcessor = processor;
    }

    @Override
    public boolean postReceiveAsync() throws IOException {
        if (isClosed.get()) {
            throw new IOException("socket channel is closed");
        }
        return ioProvider.registerInput(channel, inputCallback);
    }

    @Override
    public void setSendListener(IOArgs.IOArgsEventProcessor processor) {
        this.sendEventProcessor = processor;
    }

    @Override
    public boolean postSendAsync() throws IOException {
        if (isClosed.get()) {
            throw new IOException("socket channel is closed");
        }
        return ioProvider.registerOutput(channel, outputCallback);
    }

    public interface ChannelStatusChangedListener {
        void onStatusChanged(SocketChannel channel);
    }
}
