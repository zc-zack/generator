package com.zkys.generator.model;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import java.util.List;

/**
 * com.zkys.generator.model
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/7 14:31
 */
@Data
public class Table {

    private String tableName;

    private String tableType;

    private String tableComment;

    private List<Column> columnList;

    private String className;

    private String lowercaseClassName;

    public void setTableName(String tableName) {
        this.tableName = tableName;
        if (this.tableName != null) {
            //分隔符后一个字符转换为大写
            this.className = WordUtils.capitalize(tableName.toLowerCase(), new char[]{'_'})
                    .replace("_", "");
            //首字母变为小写
            this.lowercaseClassName = StringUtils.uncapitalize(this.className);

        }
    }
}
