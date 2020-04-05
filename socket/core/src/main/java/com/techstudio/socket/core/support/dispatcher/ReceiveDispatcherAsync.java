package com.techstudio.socket.core.support.dispatcher;

import com.techstudio.socket.core.AbstractReceivePacket;
import com.techstudio.socket.core.IOArgs;
import com.techstudio.socket.core.ReceiveDispatcher;
import com.techstudio.socket.core.Receiver;
import com.techstudio.socket.core.support.packet.StringReceivePacket;
import com.techstudio.socket.core.util.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lj
 * @since 2020/4/4
 */
public class ReceiveDispatcherAsync implements ReceiveDispatcher, IOArgs.IOArgsEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveDispatcherAsync.class);

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Receiver receiver;
    private final ReceivePacketCallback receivePacketCallback;

    private IOArgs ioArgs = new IOArgs();
    private AbstractReceivePacket<?> receivePacketTemp;
    private long total;
    private long position;
    private WritableByteChannel writableByteChannel;

    public ReceiveDispatcherAsync(Receiver receiver, ReceivePacketCallback receivePacketCallback) {
        this.receiver = receiver;
        this.receiver.setReceiveListener(this);
        this.receivePacketCallback = receivePacketCallback;
    }

    @Override
    public void start() {
        registerReceive();
    }

    @Override
    public void stop() {

    }

    @Override
    public void close() throws IOException {
        if (closed.compareAndSet(false, true)) {
            completeAssemblePacket(false);
        }
    }

    private void registerReceive() {
        try {
            receiver.postReceiveAsync();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void assemblePacket(IOArgs args) {
        // 开始读取首包
        if (receivePacketTemp == null) {
            // 设置初始值
            int length = args.readPacketLength();
            // 这里先写死string类型
            receivePacketTemp = new StringReceivePacket(length);
            writableByteChannel = Channels.newChannel(receivePacketTemp.open());
            total = length;
            position = 0;
        }
        int count;
        try {
            count = args.writeTo(writableByteChannel);
            position += count;
            // 已经完成整条数据的读取
            if (position == total) {
                completeAssemblePacket(true);
                receivePacketTemp = null;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            completeAssemblePacket(false);
        }

    }

    private void completeAssemblePacket(boolean success) {
        if (receivePacketTemp != null) {
            receivePacketCallback.onReceivePacketCompleted(receivePacketTemp);
        }
        CloseableUtils.close(logger, receivePacketTemp, writableByteChannel);
        receivePacketTemp = null;
        writableByteChannel = null;
        total = 0;
        position = 0;
    }

    @Override
    public IOArgs provideIoArgs() {
        IOArgs args = ioArgs;
        int receiveSize;
        if (receivePacketTemp == null) {
            receiveSize = 4;
        } else {
            receiveSize = (int) Math.min(total - position, args.getCapacity());
        }
        args.setLimit(receiveSize);
        return args;
    }

    @Override
    public void onConsumeFailed(IOArgs args, Exception e) {
        logger.error(e.getMessage(), e);
    }

    @Override
    public void onConsumeCompleted(IOArgs args) {
        assemblePacket(args);
        registerReceive();
    }
}