package com.zkys.generator.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * com.zkys.generator.common.utils
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/11 13:55
 */
public class TemplateUtil {

    public static String time(String format){
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String time(){
        return DateFormat.getDateTimeInstance().format(new Date());
    }
}
