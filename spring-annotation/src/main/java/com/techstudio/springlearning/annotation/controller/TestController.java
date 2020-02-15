package com.techstudio.springlearning.annotation.controller;

import com.techstudio.springlearning.annotation.event.LoginEvent;
import com.techstudio.springlearning.annotation.service.TestService;
import com.techstudio.springlearning.annotation.util.SpringContextUtils;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lj
 * @date 2020/2/10
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        ApplicationContext context = SpringContextUtils.getApplicationContext();
        TestService testService = context.getBean(TestService.class);
        return testService.hello();
    }

    @GetMapping("/login")
    public String login() {
        SpringContextUtils.publishEvent(new LoginEvent(this));
        return "login success:" + Thread.currentThread().getName();
    }

}
