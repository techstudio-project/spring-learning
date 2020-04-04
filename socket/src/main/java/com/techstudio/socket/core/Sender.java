package com.techstudio.socket.core;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public interface Sender extends Closeable {

    boolean sendAsync(IOArgs args, IOArgs.IOArgsEventListener listener) throws IOException;

}
