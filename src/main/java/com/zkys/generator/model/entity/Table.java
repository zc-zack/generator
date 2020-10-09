package com.zkys.generator.model.entity;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import java.util.List;

/**
 * com.zkys.generator.model.entity
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/10/9 10:16
 */
@Data
public class Table {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表类型
     */
    private String tableType;

    /**
     * 表备注
     */
    private String tableMark;

    /**
     * 所有列
     */
    private List<Column> columnList;

    /**
     * 类名
     */
    private String className;

    /**
     * 驼峰名
     */
    private String lowercaseClassName;

    public void setTableName(String tableName) {
        this.tableName = tableName;
        if (null != this.tableName) {
            this.className = WordUtils.capitalize(tableName.toLowerCase(), new char[]{'_'});
            this.lowercaseClassName = StringUtils.uncapitalize(this.className);
        }
    }
}
