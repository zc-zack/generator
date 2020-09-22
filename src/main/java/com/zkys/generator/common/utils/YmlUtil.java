package com.zkys.generator.common.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

/**
 * com.zkys.generator.common.utils
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/22 10:00
 */
public class YmlUtil {

    private static Yaml yaml;

    static {
        yaml = new Yaml();
    }

    public static String getString(String path) {
        InputStream in = YmlUtil.class.getResourceAsStream("/application.yml" );
        Map<String, Object> sourceMap = yaml.load(in);

        String[] keys = path.split("[.]");
        String result = null;
        for (int i = 0; i < keys.length; i++) {
            Object value = sourceMap.get(keys[i]);
            if (i < keys.length - 1) {
                sourceMap = (Map<String, Object>) value;
            } else if(value == null) {
                return null;
            } else {
                result = value.toString();
            }
        }
        return result;
    }
}
