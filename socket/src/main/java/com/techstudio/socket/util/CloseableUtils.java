package com.techstudio.socket.util;

import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author lj
 * @since 2020/4/4
 */
public class CloseableUtils {

    private CloseableUtils() {
    }

    public static void close(Logger logger, Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

    }
}
