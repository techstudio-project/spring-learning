package com.techstudio.example.component;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author lj
 * @since 2020/5/6
 */
@SpringBootApplication
public class ComponentApp {

    private static ConfigurableApplicationContext context = null;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ComponentApp.class);
        // ... customize application settings here
        context = app.run(args);
    }

}
