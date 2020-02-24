package com.techstudio.springlearning.annotation.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * @author lj
 * @date 2020/2/23
 */
public class SpringResourceUtils {

    private SpringResourceUtils() {
    }

    public static Resource[] getResources(String locationPattern) throws IOException {
        return getResourcePatternResolver().getResources(locationPattern);
    }

    public static Resource getResource(String location) {
        return getResourcePatternResolver().getResource(location);
    }

    /**
     * 基于spring的资源加载器，内部包装了DefaultResourceLoader
     *
     * @return ResourcePatternResolver
     * @see org.springframework.core.io.DefaultResourceLoader
     */
    public static ResourcePatternResolver getResourcePatternResolver() {
        return InnerResourcePatternResolver.RESOURCE_PATTERN_RESOLVER;
    }

    private static class InnerResourcePatternResolver {
        private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER =
                new PathMatchingResourcePatternResolver();
    }

}
