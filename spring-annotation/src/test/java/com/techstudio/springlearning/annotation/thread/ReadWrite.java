package com.techstudio.springlearning.annotation.thread;

/**
 * 理想情况下（代码未经重排序），A线程执行write(),B线程执行read(),当B线程能看到b变为1时，那应该也能看到a变为1
 * 但是实际上，编译器可能对代码重排序，在write()方法中b=1可能排在前面，read()的最终结果a可能还是0
 *
 * @author lj
 * @date 2020/1/20
 */
public class ReadWrite {
    private int a = 0;
    private int b = 0;

    public void write() {
        a = 1;
        b = 1;
    }

    public void read() {
        int x = b;
        int y = a;
    }
}
