package com.techstudio.springlearning.annotation.aop;

import com.techstudio.springlearning.annotation.aop.AopTest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Pointcut就类似与spring aop里面的advisor，用来匹配需要拦截的方法
 * Tips：上面匹配中，通常 "." 代表一个包名，".." 代表包及其子包，方法参数任意匹配使用两个点 ".."
 *
 * @author lj
 * @date 2020/2/14
 */
//@Aspect
//@Component
public class SystemArchitecturePointCut {

    /**
     * within：指定所在类或所在包下面的方法 （Spring AOP 独有）
     */
    @Pointcut("within(com.techstudio.springlearning.annotation.controller..*)")
    public void inWebLayer() {
    }

    /**
     * 拦截特定注解
     */
    @Pointcut("execution(* com.techstudio.springlearning.annotation..*(..))" +
            " && @annotation(AopTest)")
    public void inServiceLayer() {
    }

    /**
     * 匹配 bean 的名字（Spring AOP 独有）
     */
    @Pointcut("bean(*Dao)")
    public void inDataAccessLayer() {
    }
}
