package com.techstudio.socket.core;

import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public interface Receiver {

    boolean receiveAsync(IOArgs.IOArgsEventListener listener) throws IOException;

}
