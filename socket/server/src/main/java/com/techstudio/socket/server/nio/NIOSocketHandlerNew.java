package com.techstudio.socket.server.nio;

import com.techstudio.socket.core.Connector;
import com.techstudio.socket.core.AbstractReceivePacket;
import com.techstudio.socket.core.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;

import static com.techstudio.socket.core.AbstractPacket.TYPE_MEMORY_STRING;

/**
 * @author lj
 * @since 2020/4/4
 */
public class NIOSocketHandlerNew extends Connector {

    private static final Logger logger = LoggerFactory.getLogger(NIOSocketHandlerNew.class);
    private final File cachePath = FileUtils.getCacheDir("server");
    private final NIOSocketHandlerCallback callback;

    public NIOSocketHandlerNew(SocketChannel channel, NIOSocketHandlerCallback callback) throws IOException {
        super(channel);
        setup();
        this.callback = callback;
    }

    public void exit() {

    }

    @Override
    public void onReceiveMessage(String msg) {
        super.onReceiveMessage(msg);
        callback.onMessageReceive(this, msg);
    }

    @Override
    protected File createNewReceiveFile() {
        return FileUtils.createRandomTemp(cachePath);
    }

    @Override
    protected void onReceivedPacket(AbstractReceivePacket packet) {
        super.onReceivedPacket(packet);
        if (packet.getType() == TYPE_MEMORY_STRING) {
            String string = (String) packet.getEntity();
            logger.info("{}:{}", getKey(), string);
            callback.onMessageReceive(this, string);
        }
    }
}
