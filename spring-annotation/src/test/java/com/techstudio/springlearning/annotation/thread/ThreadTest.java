package com.techstudio.springlearning.annotation.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author lj
 * @date 2020/1/19
 */
public class ThreadTest {
    private static final Logger logger = LoggerFactory.getLogger(ThreadTest.class);

    private static Ticket ticket = null;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(2);

        for (int i = 0; i < 1; i++) {
            Thread thread = new Thread(() -> {
                try {
                    startGate.await();
                    while (ticket == null) {
                        // 在没有 volatile 修饰时一样能获取到其它线程对变量的修改
                        synchronized (Ticket.class) {

                        }
                    }
                    //logger.info("线程停止");
                    logger.info("线程停止KeepRunning：{}", ticket.isKeepRunning());

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                } finally {
                    endGate.countDown();
                }
            }, "thread-" + i);
            thread.start();
        }

        Thread thread = new Thread(() -> {
            try {
                startGate.await();
                Thread.sleep(2000);
                ticket = new Ticket(100, true);
                logger.info("设置当前线程KeepRunning：{}", ticket.isKeepRunning());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                endGate.countDown();
            }
        }, "thread-1");
        thread.start();

        logger.info("all thread is ready");
        Thread.sleep(100);

        startGate.countDown();
        endGate.await();
    }


}
