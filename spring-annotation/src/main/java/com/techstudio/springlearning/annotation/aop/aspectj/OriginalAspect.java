package com.techstudio.springlearning.annotation.aop.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author lj
 * @date 2020/2/15
 */
@Aspect
public class OriginalAspect {

    /**
     * 定义切点
     */
    @Pointcut("execution(* com.techstudio.springlite.annotation.aop.aspectj.test..*(..))")
    public void logPointCut() {
    }

    /**
     * 定义advice
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object doLog(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("执行方法之前");
        Object retVal = pjp.proceed();
        System.out.println("执行方法之后");
        return retVal;
    }

}
