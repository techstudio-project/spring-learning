package com.techstudio.springlearning.annotation.jvm;

import com.techstudio.springlearning.annotation.reflect.AbstractCglibMethodInterceptor;
import com.techstudio.springlearning.annotation.reflect.AbstractInvocationHandler;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author lj
 * @date 2020/1/26
 */
public class DynamicProxyTest {
    interface Hello {
        void say();
    }

    static class DefaultHello implements Hello, Serializable {

        @Override
        public void say() {
            System.out.println("hello world");
        }
    }

    static class DynamicProxy extends AbstractInvocationHandler<Hello> {

        public DynamicProxy(Hello original) {
            super(original);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("this is dynamic proxy");
            return method.invoke(getTarget(), args);
        }
    }

    static class CglibDynamicProxy extends AbstractCglibMethodInterceptor<DefaultHello> {

        public CglibDynamicProxy() {
            super(new DefaultHello());
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            System.out.println("this is cglib dynamic proxy");
            return proxy.invoke(getTarget(), args);
        }
    }

    public static void main(String[] args) {
        // System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
//        Hello hello = new DynamicProxy(new DefaultHello()).bind();
//        hello.say();

        DefaultHello defaultHello = new CglibDynamicProxy().bind();
        defaultHello.say();

    }
}
