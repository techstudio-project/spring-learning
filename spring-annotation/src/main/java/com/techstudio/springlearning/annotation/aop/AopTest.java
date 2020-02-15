package com.techstudio.springlearning.annotation.aop;

import java.lang.annotation.*;

/**
 * @author lj
 * @date 2020/2/14
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AopTest {
}
