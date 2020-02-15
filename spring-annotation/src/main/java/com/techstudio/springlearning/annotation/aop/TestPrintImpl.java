package com.techstudio.springlearning.annotation.aop;

import org.springframework.stereotype.Component;

/**
 * @author lj
 * @date 2020/2/13
 */
@Component
public class TestPrintImpl implements TestPrint {

    @Override
    @AopTest
    public void doSomething() {
        System.out.println("执行方法体的内容");
    }

    @Override
    public void doSomething1() {
        System.out.println("执行方法体的内容1111111111111");
    }

}
