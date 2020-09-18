package com.zkys.generator.mysql;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.zkys.generator.common.utils.MysqlTableUtil;
import com.zkys.generator.context.TemplateContext;
import com.zkys.generator.model.Table;
import com.zkys.generator.model.TableItem;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipOutputStream;

/**
 * com.zkys.generator.mysql
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/17 14:56
 */
@Log4j2
public class MysqlGenerator extends AutoGenerator {

    public static Connection connection;

    private List<Table> tableList;

    private List<TableItem> tableItemList;


    public void execute(String path) throws Exception {
        connection = initMysqlConnection();
        if (connection == null) {
            throw new Exception("连接数据库失败");
        }
        tableItemList = getTableItemList();

    }

    private Connection initMysqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DataSourceConfig dataSource = getDataSource();
            Connection connection = DriverManager
                    .getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
        return connection;
    }

    private List<TableItem> getTableItemList() throws SQLException {
        List<TableItem> tableItemList = new ArrayList<>();
        ResultSet resultSet = connection.getMetaData().getTables(
                connection.getCatalog(),
                connection.getSchema(),
                null,
                null
        );
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            if (null == tableName) {
                continue;
            }
            TableItem tableItem = new TableItem();
            tableItem.setTableName(tableName);
            tableItemList.add(tableItem);
        }
        return tableItemList;
    }

    private void generateZip(List<TableItem> tableItemList, String path) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            try(ZipOutputStream zos = new ZipOutputStream(fos)) {
                for (TableItem tableItem : tableItemList) {
                    Table table = MysqlTableUtil.getTable(connection, tableItem.getTableName());
                    if (null == table) {
                        log.error("表[{}]信息查询失败", tableItem.getTableName());
                        continue;
                    }

                }

            }

        } catch (Exception e) {

        }
    }

    private void generatorCode(TemplateContext templateContext) {

    }



}
