package com.techstudio.socket.core.support.dispatcher;

import com.techstudio.socket.core.AbstractReceivePacket;
import com.techstudio.socket.core.IOArgs;
import com.techstudio.socket.core.ReceiveDispatcher;
import com.techstudio.socket.core.Receiver;
import com.techstudio.socket.core.util.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lj
 * @since 2020/4/4
 */
public class ReceiveDispatcherAsync implements ReceiveDispatcher, IOArgs.IOArgsEventProcessor,
        PacketWriterAsync.ReceivePacketProvider {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveDispatcherAsync.class);

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Receiver receiver;
    private final ReceivePacketCallback receivePacketCallback;

    private final PacketWriterAsync packetWriter = new PacketWriterAsync(this);

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
            packetWriter.close();
        }
    }

    private void registerReceive() {
        try {
            receiver.postReceiveAsync();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public IOArgs provideIoArgs() {
        return packetWriter.takeIoArgs();
    }

    @Override
    public void onConsumeFailed(IOArgs args, Exception e) {
        logger.error(e.getMessage(), e);
    }

    @Override
    public void onConsumeCompleted(IOArgs args) {
        if (closed.get()) {
            return;
        }
        do {
            packetWriter.consumeIoArgs(args);
        } while (args.remained() && !closed.get());
        registerReceive();
    }

    @Override
    public AbstractReceivePacket takePacket(byte type, long length, byte[] headerInfo) {
        return receivePacketCallback.onArrivedNewPacket(type, length);
    }

    @Override
    public void completedPacket(AbstractReceivePacket packet, boolean isSucceed) {
        CloseableUtils.close(logger, packet);
        receivePacketCallback.onReceivePacketCompleted(packet);
    }
}