package com.techstudio.springlearning.annotation.aware;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author lj
 * @date 2020/2/10
 */
@Component
public class CustomEnvironmentAware implements EnvironmentAware {
    @Override
    public void setEnvironment(Environment environment) {

    }
}
