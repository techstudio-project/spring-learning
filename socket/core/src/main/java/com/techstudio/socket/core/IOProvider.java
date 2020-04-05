package com.techstudio.socket.core;

import java.io.Closeable;
import java.nio.channels.SocketChannel;

/**
 * @author lj
 * @since 2020/4/4
 */
public interface IOProvider extends Closeable {

    boolean registerInput(SocketChannel channel, HandleInputCallback inputCallback);

    boolean registerOutput(SocketChannel channel, HandleOutputCallback outputCallback);

    void unRegisterInput(SocketChannel channel);

    void unRegisterOutput(SocketChannel channel);

    abstract class HandleInputCallback implements Runnable {

        @Override
        public void run() {
            canProvideInput();
        }

        /**
         * 可以提供输入了
         */
        protected abstract void canProvideInput();
    }

    abstract class HandleOutputCallback implements Runnable {

        @Override
        public void run() {
            canProvideOutput();
        }

        /**
         * 可以提供输出了
         */
        protected abstract void canProvideOutput();
    }

}
