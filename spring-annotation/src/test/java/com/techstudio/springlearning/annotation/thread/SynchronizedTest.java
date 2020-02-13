package com.techstudio.springlearning.annotation.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author lj
 * @date 2020/1/20
 */
public class SynchronizedTest {

    private static final Logger logger = LoggerFactory.getLogger(SynchronizedTest.class);

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(2);

        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(() -> {
                try {
                    test();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                } finally {
                    endGate.countDown();
                }
            }, "thread-" + i);
            thread.start();
        }

        logger.info("all thread is ready");
        Thread.sleep(100);

        startGate.countDown();
        endGate.await();
    }

    public static synchronized void test() throws InterruptedException {
        logger.info("进入同步代码块");
        Thread.sleep(2000);
        logger.info("离开同步代码块");
    }

}
