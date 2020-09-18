package com.zkys.generator.common.utils;


import org.springframework.core.env.Environment;

/**
 * com.zkys.generator.common.utils
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/11 13:59
 */
public class ConfigUtil {

    public static String get(String key) {
        Environment environment = SpringContextUtil.getBean(Environment.class);
        return environment.getProperty(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        Environment environment = SpringContextUtil.getBean(Environment.class);
        return environment.getProperty(key, defaultValue);
    }
}
