package com.techstudio.example.component.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lj
 * @since 2020/5/6
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Bean
    public IntegerToEnumHoldConverterFactory enumMvcConverterFactory() {
        return new IntegerToEnumHoldConverterFactory();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(enumMvcConverterFactory());
    }
}
