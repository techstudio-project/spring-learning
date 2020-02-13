package com.techstudio.springlearning.annotation.jvm;


import com.techstudio.springlearning.annotation.thread.SingletonTest;

/**
 * @author lj
 * @date 2020/1/25
 */
public class ByteCodeTest extends BTraceTest implements IByteCodeTest {

    private static final String CONSTANT_STRING = "this is constant string";

    private static Integer integerTest = 10;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void test() {
        StackOutOfMemoryErrorTest a = new StackOutOfMemoryErrorTest();
    }

    public String test(String s) {
        GCTest gcTest = new GCTest();
        gcTest.getClass().getDeclaredMethods();
        GCTest.class.getDeclaredMethods();
        return s;
    }

    public static void test2() {
        SingletonTest singletonTest = SingletonTest.getInstance1();
    }

    static {
        // 可以超前赋值
        param_1 = "321";
        // 不能超前引用
        // System.out.println(param_1);
    }

    private static String param_1 = "123";

    public static void main(String[] args) {



        int a=0;
        System.out.println(a);
        test2();
    }

}
