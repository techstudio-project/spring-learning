package com.techstudio.socket.util;

import com.techstudio.socket.core.IOContext;
import com.techstudio.socket.core.IOContextBuilder;
import com.techstudio.socket.core.support.SelectorIOProvider;

import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public class IOContextUtils {

    public static IOContext getIoContext() {
        return InnerIOContextHolder.IO_CONTEXT;
    }

    private static class InnerIOContextHolder {
        private static final IOContext IO_CONTEXT;

        static {
            try {
                IO_CONTEXT = IOContextBuilder.newInstance()
                        .build(new SelectorIOProvider());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
