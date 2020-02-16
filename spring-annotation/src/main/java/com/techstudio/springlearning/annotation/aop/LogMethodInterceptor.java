package com.techstudio.springlearning.annotation.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 * @author lj
 * @date 2020/2/14
 */
@Component("logMethodInterceptor")
public class LogMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("implements MethodInterceptor 方法调用前执行");
        Object retVal = invocation.proceed();
        System.out.println("implements MethodInterceptor 方法调用后执行");
        return retVal;
    }
}
