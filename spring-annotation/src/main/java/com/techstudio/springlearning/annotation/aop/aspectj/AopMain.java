package com.techstudio.springlearning.annotation.aop.aspectj;

import com.techstudio.springlearning.annotation.aop.aspectj.test.OriginalClass;

/**
 * @author lj
 * @date 2020/2/15
 */
public class AopMain {

    public static void main(String[] args) {
        OriginalClass originalClass = new OriginalClass();
        originalClass.doSomething("参数1");
    }
}
