package com.techstudio.springlearning.annotation.controller;

import com.alibaba.fastjson.JSON;
import com.techstudio.springlearning.annotation.event.LoginEvent;
import com.techstudio.springlearning.annotation.jdbc.mybatis.BlogDao;
import com.techstudio.springlearning.annotation.jdbc.mybatis.dao.BlogMapper;
import com.techstudio.springlearning.annotation.service.TestService;
import com.techstudio.springlearning.annotation.util.SpringContextUtils;
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

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BlogMapper blogMapper;

    @GetMapping("/hello")
    public String hello() {
        ApplicationContext context = SpringContextUtils.getApplicationContext();
        TestService testService = context.getBean(TestService.class);

        return JSON.toJSONString(blogMapper.findAllNew());
        // return JSON.toJSONString(blogDao.findAll());

        // return testService.hello();
    }

    @GetMapping("/login")
    public String login() {
        SpringContextUtils.publishEvent(new LoginEvent(this));
        return "login success:" + Thread.currentThread().getName();
    }

}
