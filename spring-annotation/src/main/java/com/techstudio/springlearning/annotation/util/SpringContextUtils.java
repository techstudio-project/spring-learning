package com.techstudio.springlearning.annotation.util;

import com.techstudio.springlearning.annotation.AnnotationConfigApp;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringValueResolver;

/**
 * 提供访问spring上下文相关资源的静态方法，使用此类中方法的前提的spring context已经初始化完成，
 * 否则会抛出NullPointerException运行时异常，所以如果是在bean初始化时就要依赖context中的资源时还是
 * 采用implements各个Aware的方法来读取或者设置上下文
 *
 * @author lj
 * @date 2020/2/10
 * @see org.springframework.context.EnvironmentAware
 * @see org.springframework.context.EmbeddedValueResolverAware
 * @see org.springframework.context.ResourceLoaderAware
 * @see org.springframework.context.ApplicationEventPublisherAware
 * @see org.springframework.context.MessageSourceAware
 * @see org.springframework.context.ApplicationContextAware
 * @see org.springframework.context.support.AbstractApplicationContext#refresh()
 */
public class SpringContextUtils {

    private SpringContextUtils() {
    }

    /**
     * 获取ApplicationContext
     *
     * @return ConfigurableApplicationContext
     */
    public static ConfigurableApplicationContext getApplicationContext() {
        return AnnotationConfigApp.getApplicationContext();
    }

    /**
     * 获取环境配置信息
     *
     * @return Environment
     */
    public static Environment getEnvironment() {
        return getApplicationContext().getEnvironment();
    }

    /**
     * Simple strategy interface for resolving a String value.
     *
     * @return StringValueResolver
     */
    public static StringValueResolver getEmbeddedValueResolver() {
        return InnerValueResolver.RESOLVER;
    }

    /**
     * 资源加载器
     *
     * @return ResourceLoader
     */
    public static ResourceLoader getResourceLoader() {
        return getApplicationContext();
    }

    /**
     * 事件发布器
     *
     * @return ApplicationEventPublisher
     */
    public static ApplicationEventPublisher getEventPublisher() {
        return getApplicationContext();
    }

    /**
     * 发布事件
     *
     * @param event ApplicationEvent
     */
    public static void publishEvent(ApplicationEvent event) {
        getEventPublisher().publishEvent(event);
    }

    /**
     * 国际化
     *
     * @return MessageSource
     */
    public static MessageSource getMessageSource() {
        return getApplicationContext();
    }

    private static class InnerValueResolver {
        private static final StringValueResolver RESOLVER =
                new EmbeddedValueResolver(getApplicationContext().getBeanFactory());
    }
}
