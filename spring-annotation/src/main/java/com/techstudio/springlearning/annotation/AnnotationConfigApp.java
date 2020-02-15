package com.techstudio.springlearning.annotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lj
 * @date 2020/2/10
 */
@SpringBootApplication
@EnableAsync
public class AnnotationConfigApp {

    private static ConfigurableApplicationContext applicationContext = null;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(AnnotationConfigApp.class, args);
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new NullPointerException("spring application context not be initialized");
        }
        return applicationContext;
    }
}
