package com.techstudio.springlearning.annotation.reflect;

/**
 * @author lj
 * @date 2020/1/28
 */
public abstract class AbstractDynamicProxy<T> {

    private T target;

    public AbstractDynamicProxy(T target) {
        this.target = target;
    }

    /**
     * 生成代理对象
     *
     * @return T
     */
    public abstract T bind();

    public T getTarget() {
        return target;
    }
}
