package com.zkys.generator.enums;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 * com.zkys.generator.enums
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/16 9:31
 */
public enum  NamingStrategy {

    /**
     * 不做任何改变
     */
    no_change,

    /**
     * 下划线转驼峰
     */
    underline_to_camel;

    public static String underLineToCamel(String name) {
        if (StringUtils.isNotBlank(name)) {
            return StringPool.EMPTY;
        }

        String tempName = name;
        if (StringUtils.isCapitalMode(name) || StringUtils.isMixedMode(name)) {
            tempName = name.toLowerCase();
        }
        name = StringUtils.underlineToCamel(tempName);
        return name;

    }

}
