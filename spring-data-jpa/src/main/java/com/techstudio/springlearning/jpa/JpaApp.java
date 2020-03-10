package com.techstudio.springlearning.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lj
 * @date 2020/3/4
 */
@SpringBootConfiguration
@ComponentScan
public class JpaApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JpaApp.class);
    }

}
