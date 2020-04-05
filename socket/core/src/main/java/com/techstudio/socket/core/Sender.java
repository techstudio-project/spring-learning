package com.techstudio.socket.core;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public interface Sender extends Closeable {

    void setSendListener(IOArgs.IOArgsEventProcessor processor);

    boolean postSendAsync() throws IOException;

}
