package com.techstudio.springlearning;

import com.techstudio.springlearning.service.MessageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author lj
 * @date 2020/1/28
 */
public class SpringLearningApp {

    private static final String XML_LOCATION = "classpath:spring-test.xml";

    public static void main1(String[] args) {
        ApplicationContext context = annotationConfigApplicationContext();
        System.out.println("spring context is initialized");
        MessageService messageService = context.getBean(MessageService.class);
        String message = messageService.getMessage();
        System.out.println(message);
    }


    private static ApplicationContext classPathXmlApplicationContext() {
        return new ClassPathXmlApplicationContext(XML_LOCATION);
    }

    private static ApplicationContext fileSystemXmlApplicationContext() {
        return new FileSystemXmlApplicationContext(XML_LOCATION);
    }

    private static ApplicationContext genericXmlApplicationContext() {
        return new GenericXmlApplicationContext(XML_LOCATION);
    }

    private static ApplicationContext annotationConfigApplicationContext() {
        return new AnnotationConfigApplicationContext(
                "com.techstudio.springlearning");
    }

}
