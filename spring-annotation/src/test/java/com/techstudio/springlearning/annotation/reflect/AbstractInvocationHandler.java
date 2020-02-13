package com.techstudio.springlearning.annotation.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 基于jdk的动态代理，只能代理接口，即被代理类必须实现接口
 *
 * @author lj
 * @date 2020/1/27
 */
public abstract class AbstractInvocationHandler<T> extends AbstractDynamicProxy<T>
        implements InvocationHandler {

    public AbstractInvocationHandler(T target) {
        super(target);
    }

    @SuppressWarnings("unchecked")
    public T bind() {
        return (T) Proxy.newProxyInstance(getTarget().getClass().getClassLoader(),
                getTarget().getClass().getInterfaces(), this);
    }

}
