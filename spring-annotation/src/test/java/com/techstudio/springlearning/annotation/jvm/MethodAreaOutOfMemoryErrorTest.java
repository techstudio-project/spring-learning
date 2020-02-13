package com.techstudio.springlearning.annotation.jvm;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * java.lang.OutOfMemoryError: permGen space
 *
 * @author lj
 * @date 2020/1/21
 */
public class MethodAreaOutOfMemoryErrorTest {

    /**
     * 测试一 运行时常量池内存溢出测试
     * 从jdk1.7之后开始逐步“去永久代”，下面的测试在此版本之后不会看到异常
     * vm args:
     * -XX:PermSize=10m
     * -XX:MaxPermSize=10m
     *
     * @param args
     */
    public static void main1(String[] args) {
        List<String> list = new ArrayList<>();
        int i = 1;
        while (true) {
            list.add(String.valueOf(i++).intern());
        }
    }

    /**
     * 测试二 借助cglib
     * vm args:
     * -XX:PermSize=10M
     * -XX:MaxPermSize=10M
     *
     * @param args
     */
    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            });
            enhancer.create();
        }
    }

    static class OOMObject {

    }

}
