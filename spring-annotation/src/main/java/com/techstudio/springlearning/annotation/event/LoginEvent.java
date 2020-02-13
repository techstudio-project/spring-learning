package com.techstudio.springlearning.annotation.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author lj
 * @date 2020/2/10
 */
public class LoginEvent extends ApplicationEvent {

    public LoginEvent(Object source) {
        super(source);
    }
}
