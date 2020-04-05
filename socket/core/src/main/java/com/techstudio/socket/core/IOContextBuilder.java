package com.techstudio.socket.core;

/**
 * @author lj
 * @since 2020/4/4
 */
public class IOContextBuilder {

    public static IOContextBuilder newInstance() {
        return new IOContextBuilder();
    }

    public IOContext build(IOProvider ioProvider) {
        return new IOContext(ioProvider);
    }

}
