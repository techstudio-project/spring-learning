package com.techstudio.socket.core.support;

import com.techstudio.socket.core.AbstractReceivePacket;
import com.techstudio.socket.core.IOArgs;
import com.techstudio.socket.core.ReceiveDispatcher;
import com.techstudio.socket.core.Receiver;
import com.techstudio.socket.util.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public class ReceiveDispatcherAsync implements ReceiveDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveDispatcherAsync.class);

    private final Receiver receiver;
    private final ReceivePacketCallback receivePacketCallback;

    private IOArgs ioArgs = new IOArgs();
    private AbstractReceivePacket receivePacketTemp;
    private byte[] buffer;
    private int total;
    private int position;

    public ReceiveDispatcherAsync(Receiver receiver, ReceivePacketCallback receivePacketCallback) {
        this.receiver = receiver;
        this.receiver.setReceiveListener(receiveEventListener);
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

    }

    private void registerReceive() {
        try {
            receiver.receiveAsync(ioArgs);
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
            buffer = new byte[length];
            total = length;
            position = 0;
        }
        int count = args.writeTo(buffer, 0);
        if (count > 0) {
            receivePacketTemp.save(buffer, count);
            position += count;

            // 已经完成整条数据的读取
            if (position == total) {
                completeAssemblePacket();
                receivePacketTemp = null;
            }
        }
    }

    private void completeAssemblePacket() {
        receivePacketCallback.onReceivePacketCompleted(receivePacketTemp);
        CloseableUtils.close(logger, receivePacketTemp);
    }

    private IOArgs.IOArgsEventListener receiveEventListener = new IOArgs.IOArgsEventListener() {
        @Override
        public void onStarted(IOArgs args) {
            int receiveSize;
            // 当前还没有接收任何packet
            if (receivePacketTemp == null) {
                receiveSize = 4;
            } else {
                receiveSize = Math.min(total - position, args.getCapacity());
            }
            // 设置本次接收数据大小
            args.setLimit(receiveSize);
        }

        @Override
        public void onCompleted(IOArgs args) {
            assemblePacket(args);
            registerReceive();
        }
    };
}