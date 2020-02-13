package com.techstudio.springlearning.annotation.jvm;

import java.lang.ref.*;

/**
 * @author lj
 * @date 2020/1/22
 */
public class ReferenceTest {
    private String key;

    public ReferenceTest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<ReferenceTest> queue = new ReferenceQueue<>();
        WeakReference<ReferenceTest> weak1 = new WeakReference<>(new ReferenceTest("weak1"), queue);
        WeakReference<ReferenceTest> weak2 = new WeakReference<>(new ReferenceTest("weak2"), queue);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Reference<? extends ReferenceTest> reference = queue.remove();
                        System.out.println(reference);

                        ReferenceTest referenceTest = reference.get();
                        if (referenceTest != null) {
                            System.out.println(referenceTest.getKey());
                        } else {
                            System.out.println("引用为null");
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        // 等待线程就绪
        Thread.sleep(500);
        System.gc();
    }

    /**
     * @param args
     */
    public static void ref(String[] args) {
        // strong reference
        ReferenceTest strong = new ReferenceTest("123");

        // soft reference
        SoftReference<ReferenceTest> soft = new SoftReference<>(new ReferenceTest("123"));
        // 可能为null ，内存不足时可能被gc回收
        soft.get();

        // weak reference
        WeakReference<ReferenceTest> weak = new WeakReference<>(new ReferenceTest("123"));
        // 可能为null ，在执行gc后，被清除
        weak.get();

        // Phantom Reference

        // 引用队列可以与软引用、弱引用以及虚引用一起配合使用，当垃圾回收器准备回收一个对象时，
        // 如果发现它还有引用，那么就会在回收对象之前，把这个引用加入到与之关联的引用队列中去。
        // 程序可以通过判断引用队列中是否已经加入了引用，来判断被引用的对象是否将要被垃圾回收，
        // 这样就可以在对象被回收之前采取一些必要的措施。
        // 与软引用、弱引用不同，虚引用必须和引用队列一起使用
        ReferenceQueue<ReferenceTest> phantomQueue = new ReferenceQueue<>();
        // PhantomReference必须配合ReferenceQueue一起使用
        PhantomReference<ReferenceTest> phantom = new PhantomReference<>(new ReferenceTest("123"), phantomQueue);
        // 永远返回的是null，即虚引用不能取得对象实例
        phantom.get();

    }

}
