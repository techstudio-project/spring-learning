package com.techstudio.springlearning.controller;

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

    @GetMapping()
    public String hello() {
        return "spring mvc application startup";
    }

}
