package com.zkys.generator.main;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.zkys.generator.common.model.StaticValue;
import com.zkys.generator.config.MysqlStrategyConfig;
import com.zkys.generator.enums.DataTypeEnum;
import com.zkys.generator.model.entity.Column;
import com.zkys.generator.model.entity.Table;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * com.zkys.generator.main
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/10/9 11:04
 */
@Log4j2
@Data
public class MysqlGenerator {

    private PackageConfig packageConfig;

    private GlobalConfig globalConfig;

    private DataSourceConfig dataSourceConfig;

    private MysqlStrategyConfig strategyConfig;

    private List<Table> tableList;

    private Connection connection;


    public void execute() {
        try {
            Class.forName(dataSourceConfig.getDriverName());
            StaticValue.connection = DriverManager.getConnection(dataSourceConfig.getUrl(),
                    dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
            this.connection = StaticValue.connection;
        } catch (Exception e) {
            log.error("加载数据库失败");
            e.printStackTrace();
            return;
        }
        initTableList();
    }

    private void initTableList() {
        ResultSet resultSet = null;
        try {
            resultSet = connection.getMetaData().getTables(
                    connection.getCatalog(),
                    connection.getSchema(),
                    null,
                    null);
            tableList = new ArrayList<>();
            while(resultSet.next()) {
                Table table = new Table();
                String tableName = resultSet.getString("TABLE_NAME");
                table.setTableName(tableName);
                table.setColumnList(getColumnList(tableName));
                table.setTableType(resultSet.getString("TABLE_TYPE"));
                table.setTableMark(resultSet.getString("REMARKS"));
                tableList.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Column> getColumnList(String tableName) {
        ResultSet resultSet = null;
        List<Column> columnList = new ArrayList<>();
        try {
            connection.getMetaData().getColumns(
                    connection.getCatalog(),
                    connection.getSchema(),
                    tableName,
                    null);
            while(resultSet.next()) {
                if (!Objects.equals(tableName, resultSet.getString("TABLE_NAME"))) {
                    continue;
                }
                Column column = new Column();
                column.setTableName(tableName);

                column.setColumnName(resultSet.getString("COLUMN_NAME"));
                column.setDataType(resultSet.getString("TYPE_NAME"));
                column.setColumnSize(resultSet.getInt("COLUMN_SIZE"));
                column.setDecimalDigits(resultSet.getInt("DECIMAL_DIGITS"));
                column.setColumnMark(resultSet.getString("REMARKS"));

                String nullAble = resultSet.getString("IS_NULLABLE");
                if (nullAble != null) {
                    column.setNullAble("YES".equals(nullAble));
                }
                String autoIncrement = resultSet.getString("IS_AUTOINCREMENT");
                if (autoIncrement != null) {
                    column.setAutoIncrement("YES".equals(autoIncrement));
                }

                column.setAttributeType(analysisDataType(column));

                columnList.add(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnList;
    }

    private String analysisDataType(Column column) {
        if (null == column || null == column.getDataType()) {
            return Object.class.getSimpleName();
        }
        return DataTypeEnum.getJavaDataTypeByMysqlDataType(column.getDataType());
    }




}
