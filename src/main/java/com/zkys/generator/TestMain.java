package com.zkys.generator;

import com.zkys.generator.common.utils.SpringContextUtil;
import org.springframework.core.env.Environment;

/**
 * com.zkys.generator
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/21 16:29
 */
public class TestMain {

    public static void main(String[] args) {
        Environment environment = SpringContextUtil.getBean(Environment.class);
        String[] rows = environment.getProperty("generator.template.output-paths").split("\n");
        System.out.println(rows);
    }
}
