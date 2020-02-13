package com.techstudio.springlearning.annotation.jvm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.techstudio.springlearning.annotation.ConsoleUtils.booleanCommand;

/**
 * @author lj
 * @date 2020/1/24
 */
public class HeapMonitorTest {

    /**
     * vm args：
     * -Xms100m
     * -Xmx100m
     * -XX:+UseSerialGC
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        booleanCommand("is ready");
        System.out.println("test begin");

        fillHeap();

        // System.gc();

        booleanCommand("exit");
    }

    public static void fillHeap() throws InterruptedException {
        List<TestObject> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(50);
            list.add(new TestObject());
        }
        // 在方法体内调用gc，jconsole可以看到eden 和 survivor区内存占用立马下降，但是tenured gen区域仍然处于峰值状态
        // 原因是List<TestObject>没有被回收，在方法体内调用gc，List<TestObject>还处于活跃状态，是不会被gc回收的
        // 但是在方法体外调用gc，则会将其回收

        // eden->survivor->tenured gen
        System.gc();
    }

    public static class TestObject {
        // 64kb
        public byte[] placeHolder = new byte[64 * 1024];
    }


}
