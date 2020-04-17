package com.techstudio.springlearning.annotation;

import com.techstudio.springlearning.annotation.service.LoginService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lj
 * @date 2020/2/10
 */
@SpringBootApplication
@EnableAsync
@EnableFeignClients
public class AnnotationConfigApp {

    private static ConfigurableApplicationContext applicationContext = null;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(AnnotationConfigApp.class, args);
        // LoginService lo = applicationContext.getBean(LoginService.class);
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new NullPointerException("spring application context not be initialized");
        }
        return applicationContext;
    }
}
