package com.techstudio.springlearning.annotation.reflect;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * 基于cglib的动态代理，可以代理普通的类
 *
 * @author lj
 * @date 2020/1/28
 */
public abstract class AbstractCglibMethodInterceptor<T> extends AbstractDynamicProxy<T>
        implements MethodInterceptor {

    public AbstractCglibMethodInterceptor(T target) {
        super(target);
    }

    @SuppressWarnings("unchecked")
    public T bind() {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(getTarget().getClass().getClassLoader());
        enhancer.setSuperclass(getTarget().getClass());
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

}
