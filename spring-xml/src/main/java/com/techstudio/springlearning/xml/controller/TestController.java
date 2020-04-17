package com.techstudio.springlearning.xml.controller;

import com.techstudio.springlearning.xml.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lj
 * @date 2020/2/2
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return messageService.getMessage();
    }

    @GetMapping("/default")
    public ModelAndView getDefault() {
        Map<String, String> map = new HashMap<>(8);
        map.put("name", "admin");
        map.put("pwd", "123456");
        return new ModelAndView("default", map);
    }

}
