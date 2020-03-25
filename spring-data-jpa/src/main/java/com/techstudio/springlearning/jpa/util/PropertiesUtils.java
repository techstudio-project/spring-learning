package com.techstudio.springlearning.jpa.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lj
 * @date 2020/3/4
 */
public class PropertiesUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private static final Map<String, Properties> PROPERTIES_MAP = new ConcurrentHashMap<>(8);

    private PropertiesUtils() {
    }

    public static Properties loadPropertiesCache(String location) throws IOException {
        Properties properties = PROPERTIES_MAP.get(location);
        if (properties != null) {
            return properties;
        }
        properties = loadProperties(location);
        PROPERTIES_MAP.putIfAbsent(location, properties);
        return properties;
    }

    public static Properties loadProperties(String location) throws IOException {
        try (InputStream is = PropertiesUtils.class.getClassLoader()
                .getResourceAsStream(location)) {
            if (is == null) {
                throw new FileNotFoundException(location + " not found");
            }
            Properties prop = new Properties();
            prop.load(is);
            return prop;
        }
    }

    public static String getProperty(String location, String key) {
        try {
            Properties prop = loadPropertiesCache(location);
            return prop.getProperty(key);
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static String getProperty(String location, String key, String defaultValue) {
        String val = getProperty(location, key);
        return (val == null) ? defaultValue : val;
    }

}
