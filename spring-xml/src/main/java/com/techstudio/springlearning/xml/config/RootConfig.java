package com.techstudio.springlearning.xml.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.context.annotation.FilterType.ANNOTATION;
import static org.springframework.context.annotation.FilterType.REGEX;

/**
 * @author lj
 * @date 2020/2/8
 */
//@Configuration
//@ComponentScan(value = "com.techstudio.springlearning.xml",
        //excludeFilters = {@Filter(type = REGEX, pattern = "com.techstudio.springlearning.xml.controller"),
                //@Filter(type = ANNOTATION, value = EnableWebMvc.class)})
public class RootConfig {
}
