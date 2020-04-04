package com.techstudio.socket.core;

import com.techstudio.socket.core.support.SocketChannelAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * 客户端与服务端的交互抽象为一个连接
 *
 * @author lj
 * @since 2020/4/4
 */
public class Connector implements SocketChannelAdapter.ChannelStatusChangedListener {

    private static final Logger logger = LoggerFactory.getLogger(Connector.class);
    // 连接的唯一标识
    private final UUID key = UUID.randomUUID();
    private final SocketChannel channel;
    private final Sender sender;
    private final Receiver receiver;

    public Connector(SocketChannel channel, IOContext context) throws IOException {
        this.channel = channel;
        SocketChannelAdapter adapter = new SocketChannelAdapter(channel, context.getIoProvider(), this);
        this.sender = adapter;
        this.receiver = adapter;
        readNextMessage();
    }

    private void readNextMessage() {
        try {
            receiver.receiveAsync(printListener);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void onStatusChanged(SocketChannel channel) {

    }

    private IOArgs.IOArgsEventListener printListener = new IOArgs.IOArgsEventListener() {
        @Override
        public void onStarted(IOArgs args) {

        }

        @Override
        public void onCompleted(IOArgs args) {
            onReceiveMessage(args.buffer2String());
            readNextMessage();
        }
    };

    public void onReceiveMessage(String msg) {
        logger.info("{}:{}", key, msg);
    }
}
