package com.techstudio.springlearning.annotation.aware;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

/**
 * @author lj
 * @date 2020/2/10
 */
@Component
public class CustomMessageSourceAware implements MessageSourceAware {
    @Override
    public void setMessageSource(MessageSource messageSource) {

    }
}
