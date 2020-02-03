package com.techstudio.springlearning.service.impl;

import com.techstudio.springlearning.service.MessageService;
import com.techstudio.springlearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lj
 * @date 2020/1/28
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private UserService userService;

    @Override
    public String getMessage() {
        return "hello world";
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
