package com.techstudio.springlearning.annotation.aop.aspectj.test;

/**
 * @author lj
 * @date 2020/2/15
 */
public class OriginalClass {


    public String doSomething(String param1) {
        System.out.println("这是执行的被拦截方法");
        return "success";
    }

}
