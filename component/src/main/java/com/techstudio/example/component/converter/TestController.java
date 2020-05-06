package com.techstudio.example.component.converter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lj
 * @since 2020/5/6
 */
@RestController
public class TestController {


    @GetMapping("/converter_test")
    public String convert(@RequestParam EnumTest enumTest) {
        return enumTest.getName();
    }
}
