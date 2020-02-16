package com.techstudio.springlearning.annotation.aop;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author lj
 * @date 2020/2/13
 */
@Component("logAfterReturningAdvice")
public class LogAfterReturningAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("implements AfterReturningAdvice，在方法调用后执行");
    }
}
