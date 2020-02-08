package com.techstudio.springlearning.service.impl;

import com.techstudio.springlearning.service.InfoService;
import com.techstudio.springlearning.service.MessageService;
import com.techstudio.springlearning.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author lj
 * @date 2020/1/28
 */
@Service
public class MessageServiceImpl implements MessageService {

    private final UserService userService;

    private InfoService infoService;

    public MessageServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getMessage() {
        return "hello world";
    }

    public InfoService getInfoService() {
        return infoService;
    }

    public void setInfoService(InfoService infoService) {
        this.infoService = infoService;
    }
}
