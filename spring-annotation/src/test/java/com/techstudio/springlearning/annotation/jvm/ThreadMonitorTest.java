package com.techstudio.springlearning.annotation.jvm;


import java.io.IOException;

import static com.techstudio.springlearning.annotation.ConsoleUtils.booleanCommand;

/**
 * @author lj
 * @date 2020/1/24
 */
public class ThreadMonitorTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        booleanCommand("is ready");

//        nonLimitedLoop();
//
//        Object obj = new Object();
//        lockWait(obj);
//
//        booleanCommand("notify thread-lockWait");
//        lockNotify(obj);

        deadLock();

        booleanCommand("exit");
    }

    /**
     * 线程死循环
     */
    public static void nonLimitedLoop() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                }
            }
        }, "thread-nonLimitedLoop");
        thread.start();
    }

    public static void lockWait(final Object obj) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                    try {
                        obj.wait();
                        System.out.println("我又被唤醒了");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "thread-lockWait");
        thread.start();
    }

    public static void lockNotify(final Object obj) {
        synchronized (obj) {
            obj.notify();
        }
    }

    public static void deadLock() {
        for (int i = 0; i < 100; i++) {
            new Thread(new Sum(1, 2), "thread-a-" + i).start();
            new Thread(new Sum(2, 1), "thread-a-" + i).start();
        }
    }

    public static void compare() {
        Integer a = Integer.valueOf(127);
        Integer b = Integer.valueOf(127);
        System.out.println(a == b);
        Integer a1 = Integer.valueOf(128);
        Integer b1 = Integer.valueOf(128);
        System.out.println(a1 == b1);
    }

    public static class Sum implements Runnable {

        private int a;
        private int b;

        public Sum(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            synchronized (Integer.valueOf(a)) {
                synchronized (Integer.valueOf(b)) {
                    System.out.println(a + b);
                }
            }

        }
    }


}
