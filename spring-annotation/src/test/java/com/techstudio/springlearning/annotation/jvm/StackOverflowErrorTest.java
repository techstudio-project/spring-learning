package com.techstudio.springlearning.annotation.jvm;

/**
 * java.lang.StackOverflowError
 *
 * @author lj
 * @date 2020/1/21
 */
public class StackOverflowErrorTest {

    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    /**
     * OutOfMemoryError 与 StackOverflowError 存在相互交叉
     * 前者是指当虚拟机栈要申请更大内存空间时，没有足够内存；
     * 后者是指线程请求的栈深度超过了虚拟机允许的最大栈深度
     * 当栈空间不足时，到底是内存太小，还是栈空间太大，其本质是同一间事情
     * <p>
     * vm args: -Xss128k 设置栈空间大小
     * <p>
     * 一般情况下，使用虚拟机默认的参数，栈深度在大多数情况下（每个方法压入栈的帧大小并不一样，所以只能是大多数）
     * 达到1000-2000是没有问题的，对于正常的调用（递归）完全够用
     *
     * @param args
     */
    public static void main(String[] args) {
        StackOverflowErrorTest stackOverflowErrorTest = new StackOverflowErrorTest();
        try {
            stackOverflowErrorTest.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length:" + stackOverflowErrorTest.stackLength);
            throw e;
        }

    }

}
