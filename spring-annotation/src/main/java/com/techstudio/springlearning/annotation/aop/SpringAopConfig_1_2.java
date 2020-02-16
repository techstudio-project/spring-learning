package com.techstudio.springlearning.annotation.aop;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1.ProxyFactoryBean 配合advice拦截整个类里面的方法
 * 2.用advisor替换直接使用advice来达到更细粒度的拦截控制
 * 3.BeanNameAutoProxyCreator、DefaultAdvisorAutoProxyCreator替换ProxyFactoryBean来达到更好的非侵入性
 *
 * @author lj
 * @date 2020/2/13
 */
@Configuration
public class SpringAopConfig_1_2 {

    //@Bean
    public ProxyFactoryBean testPrintFactoryBean() throws ClassNotFoundException {
        ProxyFactoryBean bean = new ProxyFactoryBean();
        bean.setInterfaces(OrderService.class);
        bean.setTarget(new OrderServiceImpl());
        // 基于advice直接拦截，这种方式粒度只能到类，及类中的方法都将被拦截
        bean.setInterceptorNames("logMethodBeforeAdvice", "logAfterReturningAdvice");
        return bean;
    }

    //@Bean
    public ProxyFactoryBean testPrintFactoryBean1() throws ClassNotFoundException {
        ProxyFactoryBean bean = new ProxyFactoryBean();
        bean.setInterfaces(OrderService.class);
        bean.setTarget(new OrderServiceImpl());
        // 这里配置的Advisor将拦截粒度控制在方法
        bean.setInterceptorNames("testPrintMethodInterceptor");
        return bean;
    }

    //@Bean
    public ProxyFactoryBean testPrintFactoryBean2() throws ClassNotFoundException {
        ProxyFactoryBean bean = new ProxyFactoryBean();
        bean.setInterfaces(OrderService.class);
        bean.setTarget(new OrderServiceImpl());
        // 实现了 MethodInterceptor接口的拦截器
        bean.setInterceptorNames("logMethodInterceptor");
        return bean;
    }


    /**
     * 通过Advisor来控制拦截的方法，这是spring基于advice（aop联盟的接口）的扩展
     *
     * @param advice
     * @return
     */
    //@Bean("testPrintMethodInterceptor")
    public NameMatchMethodPointcutAdvisor testPrintMethodInterceptor(LogAfterReturningAdvice advice) {
        NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
        advisor.setAdvice(advice);
        advisor.setMappedNames("doSomething");
        return advisor;
    }

    @Bean
    public RegexpMethodPointcutAdvisor LogRegexpMethodPointcutAdvisor(LogMethodBeforeAdvice advice) {
        RegexpMethodPointcutAdvisor advisor = new RegexpMethodPointcutAdvisor();
        advisor.setAdvice(advice);

        // 正则匹配规则
        // . 任意的单一字符
        // + 前一个字符出现一次或多次
        // * 表示前一个字符出现一次或者多次

        // OrderService.getOrder方法将被匹配
        advisor.setPattern("com.techstudio.springlearning.annotation.aop.OrderService.getOrder.*");
        return advisor;
    }

    //@Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
        BeanNameAutoProxyCreator proxyCreator = new BeanNameAutoProxyCreator();
        proxyCreator.setInterceptorNames("logMethodInterceptor");
        proxyCreator.setBeanNames("*Impl");
        return proxyCreator;
    }


    /**
     * 该bean的作用是使用ioc容器中所有的advisor来进行匹配
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }
}
