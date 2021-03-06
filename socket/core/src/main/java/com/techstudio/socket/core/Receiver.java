package com.techstudio.socket.core;

import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public interface Receiver {

    void setReceiveListener(IOArgs.IOArgsEventProcessor processor);

    boolean postReceiveAsync() throws IOException;

}
