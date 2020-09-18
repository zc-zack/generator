package com.zkys.generator;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.zkys.generator.common.model.BaseModel;
import com.zkys.generator.model.Column;
import com.zkys.generator.model.Table;

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
    public static void main(String[] args) throws SQLException {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setSuperEntityClass(BaseModel.class);
        strategyConfig.setRestControllerStyle(true);
    }
}
