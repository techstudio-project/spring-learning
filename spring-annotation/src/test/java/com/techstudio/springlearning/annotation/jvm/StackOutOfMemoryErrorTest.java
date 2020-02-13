package com.techstudio.springlearning.annotation.jvm;

/**
 * @author lj
 * @date 2020/1/21
 */
public class StackOutOfMemoryErrorTest {

    private void keepRunning() {
        while (true) {

        }
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    keepRunning();
                }
            });
            thread.start();
        }
    }

    /**
     * vm args :
     * -Xss2m
     *
     * @param args
     */
    public static void main(String[] args) {
        StackOutOfMemoryErrorTest stackOutOfMemoryErrorTest = new StackOutOfMemoryErrorTest();
        stackOutOfMemoryErrorTest.stackLeakByThread();
    }

}
