package com.zkys.generator.main;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.zkys.generator.common.model.StaticValue;
import com.zkys.generator.common.utils.YmlUtil;
import com.zkys.generator.config.MysqlStrategyConfig;
import com.zkys.generator.config.PackageConfig;
import com.zkys.generator.context.TemplateContext;
import com.zkys.generator.enums.DataTypeEnum;
import com.zkys.generator.model.entity.Column;
import com.zkys.generator.model.entity.Table;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    static {
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
    }

    private PackageConfig packageConfig;

    private GlobalConfig globalConfig;

    private DataSourceConfig dataSourceConfig;

    private MysqlStrategyConfig strategyConfig;

    private List<Table> tableList;

    private Connection connection;


    /**
     * 执行代码代码生成
     */
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
        generateZip(tableList, "./entity.zip");
    }

    /**
     * 获取表
     */
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
                table.setComment(resultSet.getString("REMARKS"));
                tableList.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Column> getColumnList(String tableName) {
        ResultSet resultSet;
        List<Column> columnList = new ArrayList<>();
        try {
            resultSet = connection.getMetaData().getColumns(
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
                column.setComment(resultSet.getString("REMARKS"));

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

    /**
     * sql 数据转 java数据类型
     * @param column
     * @return
     */
    private String analysisDataType(Column column) {
        if (null == column || null == column.getDataType()) {
            return Object.class.getSimpleName();
        }
        return DataTypeEnum.getJavaDataTypeByMysqlDataType(column.getDataType().toLowerCase());
    }

    /**
     * 生成文件
     *
     * @param tableList
     * @param path
     */
    private void generateZip(List<Table> tableList, String path) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            try (ZipOutputStream zos = new ZipOutputStream(fos)) {
                for (Table table : tableList) {
                    TemplateContext context = new TemplateContext();
                    context.buildTable(table)
                            .buildPackage(packageConfig)
                            .buildGlobal(globalConfig)
                            .buildStrategy(strategyConfig)
                            .buildDynamicVariables(table);
                    generatorCode(context, zos);
                }
            }
        } catch (Exception e) {
            log.error("创建文件失败");
            e.printStackTrace();
        }
    }

    /**
     * 生成代码
     * @param templateContext
     * @param zos
     */
    private void generatorCode(TemplateContext templateContext, ZipOutputStream zos) {
        System.out.println(templateContext.getMap());
        VelocityContext velocityContext = new VelocityContext(templateContext.getMap());
        Map<String, String> outputPathMap = parseTemplateOutputPaths(templateContext.getDynamicPathVariables());
        for (Map.Entry<String, String> entry: outputPathMap.entrySet()) {
            Template template = Velocity.getTemplate(entry.getKey(), "UTF-8");
            try (StringWriter writer = new StringWriter()){
                template.merge(velocityContext, writer);
                zos.putNextEntry(new ZipEntry(entry.getValue()));
                IOUtils.write(writer.toString(), zos, "UTF-8");
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 解析路径
     * @param dynamicVariables
     * @return
     */
    private Map<String, String> parseTemplateOutputPaths(Map<String, String> dynamicVariables) {
        String pathString = YmlUtil.getString("generator.template.output-paths");
        String templateBasePath = YmlUtil.getString("generator.template.base-path");
        Map<String, String> outputPathMap = new HashMap<>();
        if (null == pathString) {
            return null;
        }
        String[] paths = pathString.split("\n");
        for (String path : paths) {
            int index = path.indexOf(":");
            if (index == -1) {
                continue;
            }
            String fileName = path.substring(0, index).trim();
            try {
                String outputPath = replace(path.substring(index + 1).trim(), dynamicVariables);
                outputPathMap.put(templateBasePath + "/" + fileName, outputPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outputPathMap;
    }

    private String replace(String pattern, Map<String, String> context) throws Exception {
        char[] patternChars = pattern.toCharArray();
        StringBuffer valueBuffer = new StringBuffer();
        StringBuffer variableNameBuffer = null;
        boolean inVariable = false;
        for (int i = 0; i < patternChars.length; i++) {
            if (!inVariable && '{' == patternChars[i]) {
                inVariable = true;
                variableNameBuffer = new StringBuffer();
                continue;
            }
            if (inVariable && '}' == patternChars[i]) {
                inVariable = false;
                String variable = context.get(variableNameBuffer.toString());
                valueBuffer.append(variable == null ? "null" : variable);
                variableNameBuffer = null;
                continue;
            }
            if ('\\' == patternChars[i] && ++i == patternChars.length) {
                throw new Exception("转义符 '\\' 后缺少字符");
            }
            StringBuffer activeBuffer = inVariable ? variableNameBuffer : valueBuffer;
            activeBuffer.append(patternChars[i]);
        }
        if (variableNameBuffer != null) {
            throw new Exception("结尾缺少 '}' ");
        }
        return valueBuffer.toString();
    }

}
