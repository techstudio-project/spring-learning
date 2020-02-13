package com.techstudio.springlearning.annotation.thread;

/**
 * @author lj
 * @date 2020/1/20
 */
public class SingletonTest {

    // volatile 保证其它线程在a处对singletonTest的可见性，就不用每次都访问同步代码块了
    // 即使没有volatile修饰变量，单例也是没有问题的，只是并发下其它线程都会执行到synchronized修饰的
    // 代码块，
    // 同步代码块的可见性保证：对一个变量执行unlock之前，必须先把本地变量同步回主内存，
    // 所以其它线程再次进入时可以获得最新的变量值，所以第二次检查是必需的
    // ------------------------以上说法不完全正确-----------
    // 没有volatile是会存在问题的（指令重排序问题）
    // 当有线程进入同步代码块c时，即new SingletonTest(),会分为一下几步
    // 1.分配内存
    // 2.初始化对象
    // 3.singletonTest 指向内存
    // 如果发生指令重排序，执行顺序可能是132，当要执行2时，突然有其它线程进入a,这时候会判断singletonTest！=null
    // 即直接返回一个未初始化好的对象，这样就存在问题
    //
    // 所以在单例中volatile有两个作用：可见性，禁止指令重排序
    private static volatile SingletonTest singletonTest;

    // 对比反编译的结果
    private static SingletonTest singletonTestNon;

    private static SingletonTest _singletonTest;

    private SingletonTest() {
        System.out.println("execute init");
    }

    public static SingletonTest getInstance() {
        if (singletonTest == null) { // a
            synchronized (SingletonTest.class) {
                if (singletonTest == null) { // b
                    singletonTest = new SingletonTest(); // c
                }
            }
        }
        return singletonTest;
    }

    public static SingletonTest getInstanceNon() {
        if (singletonTestNon == null) { // a
            synchronized (SingletonTest.class) {
                if (singletonTestNon == null) { // b
                    singletonTestNon = new SingletonTest(); // c
                }
            }
        }
        return singletonTestNon;
    }

    /**
     * 借助于jvm在初始化类时，在多线程并发的情况下保证只有一个线程执行静态代码块
     * clinit（），将类中的静态变量、静态代码块，合并后产生的函数，可以理解为类的构造方法（区别于实例的构造方法init）
     * -----------
     * lazy模式、线程安全
     * 因为只有在调用静态方法时，才会执行类的初始化，但是如果调用本类的其它的静态方法（未用到单例对象），
     * 也会将对象实例化，这时就没有lazy效果了，有另外一个方法：静态内部类
     */
    static {
        //_singletonTest = new SingletonTest();
    }

    public static SingletonTest getInstance1() {
        return _singletonTest;
    }

    /**
     * 可以防止本类的其它方法触发单例对象生成
     */
    private static class InnerSingletonClass {
        private static final SingletonTest singletonTest = new SingletonTest();
    }

    /**
     * lazy、线程安全
     *
     * @return
     */
    public static SingletonTest getInstance2() {
        return InnerSingletonClass.singletonTest;
    }

    public static void test3(){
        System.out.println(1);
    }
}
