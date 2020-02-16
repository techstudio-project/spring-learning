package com.techstudio.springlearning.annotation.aop;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author lj
 * @date 2020/2/13
 */
@Component("logMethodBeforeAdvice")
public class LogMethodBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("implements MethodBeforeAdvice 在方法调用前执行");
    }
}
