package com.zkys.generator;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.zkys.generator.common.model.BaseModel;
import com.zkys.generator.config.MyStrategyConfig;
import com.zkys.generator.model.Column;
import com.zkys.generator.model.Table;
import com.zkys.generator.mysql.MysqlGenerator;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * com.zkys.generator
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/7 14:57
 */
public class Main {
    public static void main(String[] args) throws Exception {
        MysqlGenerator mysqlGenerator = new MysqlGenerator();

        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setSwagger2(true);
        globalConfig.setAuthor("zhangC");
        mysqlGenerator.setGlobalConfig(globalConfig);

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://139.224.115.237:3306/generator?characterEncoding=utf-8&useSSL=false&useUnicode=true");
        dsc.setUsername("root");
        dsc.setPassword("zhangC");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        mysqlGenerator.setDataSource(dsc);

        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName("generator");
        packageConfig.setParent("com.zkys");
        mysqlGenerator.setPackageInfo(packageConfig);

        MyStrategyConfig strategyConfig = new MyStrategyConfig();
        strategyConfig.setImplController(true);
        strategyConfig.setSuperEntityClass(BaseModel.class);
        mysqlGenerator.setStrategy(strategyConfig);
        mysqlGenerator.execute("./entity.zip");


    }
}
