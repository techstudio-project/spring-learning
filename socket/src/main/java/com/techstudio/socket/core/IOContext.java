
package com.techstudio.socket.core;

/**
 * io上下文，应该设置成单例，供整个应用全局使用
 *
 * @author lj
 * @since 2020/4/4
 */
public class IOContext {

    private final IOProvider ioProvider;

    public IOContext(IOProvider ioProvider) {
        this.ioProvider = ioProvider;
    }

    public IOProvider getIoProvider() {
        return ioProvider;
    }
}
