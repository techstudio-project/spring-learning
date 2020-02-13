package com.techstudio.springlearning.annotation.jvm;

import com.techstudio.springlearning.annotation.thread.SingletonTest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lj
 * @date 2020/1/27
 */
public class AtomicOperationTest {

    private static final String STRING_CONSTANT = "this is string constant";
    private static final GCTest GC_TEST = new GCTest();

    private static GenericTest genericTest;

    private ByteCodeTest byteCodeTest;

    public static void main(String[] args) {
        SingletonTest.test3();
    }

    public static void test() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        atomicInteger.incrementAndGet();
        // ?
        boolean a = atomicInteger.compareAndSet(0, 2);
        System.out.println(a);// true
        System.out.println(atomicInteger.get());// 2
    }

}
