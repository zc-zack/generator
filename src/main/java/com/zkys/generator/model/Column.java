package com.zkys.generator.model;

import lombok.Data;

/**
 * com.zkys.generator.model
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/7 14:38
 */
@Data
public class Column {
    private String tableName;
    private String columnName;
    private String dataType;
    private String columnComment;
    private Integer columnSize;
    private Integer decimalDigits;
    private boolean nullAble;
    private boolean autoIncrement;

    private String attributeName;
    private String uppercaseAttributeName;
    private String attributeType;
}
