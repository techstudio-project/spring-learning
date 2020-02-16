package com.techstudio.springlearning.annotation.aop;

import org.springframework.stereotype.Component;

/**
 * @author lj
 * @date 2020/2/13
 */
@Component
public class OrderServiceImpl implements OrderService {

    @Override
    public String getOrder() {
        System.out.println("获得order=123456");
        return "success";
    }
}
