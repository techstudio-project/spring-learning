package com.techstudio.springlearning.xml;

import com.techstudio.springlearning.xml.config.RootConfig;
import com.techstudio.springlearning.xml.config.WebMvcConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;

/**
 * 此类的作用是通过注解的方式初始化spring的DispatcherServlet
 *
 * @author lj
 * @date 2020/2/8
 */
public class CustomAnnotationConfigDispatcherServletInitializer
        //extends AbstractAnnotationConfigDispatcherServletInitializer
{

    //@Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    //@Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebMvcConfig.class};
    }

    //@Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    //@Override
    protected Filter[] getServletFilters() {
        return new Filter[]{characterEncodingFilter()};
    }

    private CharacterEncodingFilter characterEncodingFilter() {
        return new CharacterEncodingFilter(StandardCharsets.UTF_8.toString());
    }
}
