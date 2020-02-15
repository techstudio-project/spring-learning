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
        if(invocation.getMethod().getName().equals("doSomething")){
            System.out.println("this this MethodInterceptor");
        }
        return invocation.proceed();
    }
}
