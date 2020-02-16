package com.techstudio.springlearning.annotation.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author lj
 * @date 2020/2/15
 */
public class AopMain {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                "com.techstudio.springlearning.annotation.aop");

        OrderService orderService = context.getBean(OrderService.class);
        orderService.getOrder();
    }
}
