package com.techstudio.springlearning.xml.controller;

import com.techstudio.springlearning.xml.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lj
 * @date 2020/2/2
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MessageService messageService;

    @GetMapping("hello")
    public String hello() {
        return messageService.getMessage();
    }

}
