package com.techstudio.springlearning.annotation.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 定义advice，就是实际进行拦截后的业务逻辑
 *
 * @author lj
 * @date 2020/2/14
 */
//@Aspect
//@Component
public class AdviceExample {

    /**
     * 引用SystemArchitecturePointCut中的pointcut，
     * 也可以直接在advice中写pointcut的表达式，但是得明白两者的概念是不同的
     */
    @Before("com.techstudio.springlearning.annotation.aop.SystemArchitecturePointCut.inServiceLayer()")
    public void printLog(JoinPoint joinPoint) {
        System.out.println("这是调用方法之前执行的");
    }

}
