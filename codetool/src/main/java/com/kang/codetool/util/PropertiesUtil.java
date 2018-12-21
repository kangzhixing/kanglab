package com.kang.codetool.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Load setting.properties
 */
public class PropertiesUtil {

    private static Properties prop = new Properties();
    private static Log logger = LogFactory.getLog(PropertiesUtil.class);

    static {
        init();
    }

    private static void init() {

        InputStream stream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            prop.load(stream);
        } catch (IOException e) {
            logger.error("properties 初始化配置失败！", e);
        }

    }

    public static String get(String key) {
        return prop.getProperty(key);
    }

}
