package com.zkys.generator.table;

import com.zkys.generator.enums.DataTypeEnum;
import com.zkys.generator.model.Column;
import com.zkys.generator.model.Table;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * com.zkys.generator.table
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/7 14:29
 */
@Service
public class MysqlTableService {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public Table getTale(String tableName) throws Exception{
        Connection connection = getConnection();
        Table table = getMateDataTable(connection, tableName);
        if (table == null) {
            return null;
        }
        List<Column> columnList = getColumnList(connection, tableName);
        table.setColumnList(columnList);
        return table;
    }

    private Table getMateDataTable(Connection connection, String tableName) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getTables(
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
            table.setTableType(resultSet.getString("TABLE_TYPE"));
            table.setTableComment(resultSet.getString("REMARKS"));
            return table;
        }
        return null;
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }

    private List<Column> getColumnList(Connection connection, String tableName) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getColumns(
                connection.getCatalog(),
                connection.getSchema(),
                tableName,
                null);
        List<Column> columnList = new ArrayList<>();
        while (resultSet.next()) {
            if (tableName.equals(resultSet.getString("TABLE_NAME"))) {
                continue;
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
            column.setAttributeName(analysisDataType(column));
            columnList.add(column);
        }
        return columnList;
    }

    private String analysisDataType(Column column) {
        if (null == column || null == column.getDataType()) {
            return null;
        }
        return DataTypeEnum.getJavaDataType(column.getDataType()
                .toLowerCase()
                .replace("unsigned", "")
                .trim());
    }
}
