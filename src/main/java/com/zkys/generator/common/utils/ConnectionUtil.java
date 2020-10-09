package com.zkys.generator.common.utils;

import com.zkys.generator.common.model.StaticValue;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * com.zkys.generator.common.utils
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/10/9 10:41
 */
@Log4j2
public class ConnectionUtil {

    public void initConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("加载mysql失败");
            e.printStackTrace();
        }
        String url = YmlUtil.getString("spring.datasource.url");
        String username = YmlUtil.getString("spring.datasource.username");
        String password = YmlUtil.getString("spring.datasource.password");
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            if (null != connection) {
                StaticValue.connection = connection;
            }
        } catch (SQLException e) {
            log.error("获取mysql连接失败");
            e.printStackTrace();
        }

    }
}
