package com.techstudio.springlearning.annotation.jvm;

/**
 * @author lj
 * @date 2020/1/21
 */
public class GCTest {

    public static final int _1m = 1024 * 1024;
    public Object instance = null;
    public static GCTest GC_TEST = null;

    /**
     * 验证gc算法不是 “引用计数器” 来判断是否对对象进行回收
     * 原理：通过对象内的字段进行相互引用，再执行gc，结果两个对象均被回收
     * <p>
     * vm args:
     * -XX:+PrintGC 输出GC日志
     * -XX:+PrintGCDetails 输出GC的详细日志
     * -XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式）
     * -XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
     * -XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
     * -Xloggc:../logs/gc.log 日志文件的输出路径
     *
     * @param args
     */
    public static void main1(String[] args) {
        GCTest gcTestA = new GCTest();
        GCTest gcTestB = new GCTest();
        gcTestA.instance = gcTestB;
        gcTestB.instance = gcTestA;
        gcTestA = null;
        gcTestB = null;
        System.gc();
    }

    public void alive() {
        System.out.println("yes,i am alive");
    }

    /**
     * 该方法最多只会被系统调用一次（即自救机会只有一次）
     * <p>
     * 重写此方法，验证在调用gc（）后，对象还有一次逃逸的机会，
     * 下面方法是采用又建立起对对象的引用，来阻止gc回收它
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method is executed");
        GCTest.GC_TEST = this;
    }

    /**
     * 模拟gc过程 （可达性分析算法）
     * <p>
     * 原理：通过一系列称为gc roots的对象作为起始点，从这些对象向下搜索，搜索走过的路径就是引用链
     * 当一个对象到gc roots没有任何引用链，则对象不可用，将被gc回收
     * <p>
     * 可以作为gc roots的对象如下
     * 虚拟机栈（栈帧中的本地变量表）中引用的对象
     * 方法区中类静态属性引用的对象
     * 方法区中常量引用的对象
     * 本地方法栈中JNI（native方法）引用的对象
     * <p>
     * 下面验证即使对象在不可达的情况下，也并非绝对会被gc回收（对象自救）
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        GCTest.GC_TEST = new GCTest();

        // 清除引用（对象不可达）
        GCTest.GC_TEST = null;
        // 垃圾回收
        System.gc();
        // 系统执行finalize（）的优先级很低，等待一下
        Thread.sleep(500);
        // 理论上已经执行完我们自己重写的finalize（）了
        if (GC_TEST == null) {
            System.out.println("no,i am dead");
        } else {
            System.out.println("yes,i am alive");
        }

        // 再模拟一次，验证finalize（）是否会第二次执行
        GCTest.GC_TEST = null;
        System.gc();
        Thread.sleep(500);
        if (GC_TEST == null) {
            System.out.println("no,i am dead");
        } else {
            System.out.println("yes,i am alive");
        }
    }

}
