package com.techstudio.example.component.converter;

import java.lang.annotation.*;

/**
 * @author lj
 * @since 2020/5/6
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnumConvertMethod {
}
