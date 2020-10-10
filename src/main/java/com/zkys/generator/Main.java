package com.zkys.generator;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.zkys.generator.common.model.BaseModel;
import com.zkys.generator.config.MysqlStrategyConfig;
import com.zkys.generator.main.MysqlGenerator;
import sun.security.mscapi.CPublicKey;

/**
 * com.zkys.generator
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/10/9 11:06
 */
public class Main {
    public static void main(String[] args) {

        MysqlGenerator mysqlGenerator = new MysqlGenerator();

        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setAuthor("zhangc");
        globalConfig.setSwagger2(true);
        mysqlGenerator.setGlobalConfig(globalConfig);

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://139.224.115.237:3306/generator?characterEncoding=utf-8&useSSL=false&useUnicode=true");
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("zhangC1997");
        mysqlGenerator.setDataSourceConfig(dataSourceConfig);

        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.zkys");
        packageConfig.setModuleName("generator");
        mysqlGenerator.setPackageConfig(packageConfig);

//        String templatePath = "/templates/mapper.xml.vm";

        MysqlStrategyConfig strategyConfig = new MysqlStrategyConfig();
        strategyConfig.setSuperEntityClass(BaseModel.class);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setRestControllerStyle(true);
        mysqlGenerator.setStrategyConfig(strategyConfig);

        mysqlGenerator.execute();
    }
}
