package com.techstudio.springlearning.annotation.service;

import com.techstudio.springlearning.annotation.event.LoginEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author lj
 * @date 2020/2/10
 */
@Service
public class LoginService implements InitializingBean, ApplicationListener<LoginEvent> {

    @Override
    @Async
   // @EventListener 注解或者 继承接口
    public void onApplicationEvent(LoginEvent event) {
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("收到login事件，来自：" + event.getSource());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
