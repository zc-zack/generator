package com.zkys.generator.common.utils;

import com.zkys.generator.enums.DataTypeEnum;
import com.zkys.generator.model.Column;
import com.zkys.generator.model.Table;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * com.zkys.generator.common.utils
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/17 16:36
 */
@Log4j2
public class MysqlTableUtil {

    public static Table getTable(Connection connection, String tableName) {
        Table table = getMetaDataTable(connection, tableName);
        if (null == table) {
            return null;
        }
        List<Column> columnList = getColumnList(connection, tableName);
        table.setColumnList(columnList);
        return table;
    }

    private static Table getMetaDataTable(Connection connection, String tableName) {
        ResultSet resultSet = null;
        try {
            resultSet = connection.getMetaData().getTables(
                    connection.getCatalog(),
                    connection.getSchema(),
                    tableName,
                    null);
            if (resultSet.next()) {
                if (!Objects.equals(tableName, resultSet.getString("TABLE_NAME"))) {
                    return null;
                }
                Table table = new Table();
                table.setTableName(tableName);
                table.setTableComment(resultSet.getString("TABLE_TYPE"));
                table.setTableComment(resultSet.getString("REMARKS"));
                return table;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("查询表失败");
            return null;
        }
        return null;
    }

    private static List<Column> getColumnList(Connection connection, String tableName) {
        try {
            ResultSet resultSet = connection.getMetaData().getColumns(
                    connection.getCatalog(),
                    connection.getSchema(),
                    tableName,
                    null);
            List<Column> columnList = new ArrayList<>();
            while (resultSet.next()) {
                if (!Objects.equals(tableName, resultSet.getString("TABLE_NAME"))) {
                    continue;
                }
            }
            Column column = new Column();
            column.setTableName(tableName);
            column.setColumnName(resultSet.getString("COLUMN_NAME"));
            column.setDataType(resultSet.getString("TYPE_NAME"));
            column.setColumnSize(resultSet.getInt("COLUMN_SIZE"));
            column.setDecimalDigits(resultSet.getInt("DECIMAL_DIGITS"));
            column.setColumnComment(resultSet.getString("REMARKS"));

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

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("查询字段名失败");
            return null;
        }
        return null;
    }

    private static String analysisDataType(Column column) {
        if (null == column || null == column.getDataType()) {
            return Object.class.getSimpleName();
        }
        return DataTypeEnum.getJavaDataType(column.getDataType());
    }
}
