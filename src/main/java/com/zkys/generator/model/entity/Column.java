package com.zkys.generator.model.entity;

import lombok.Data;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * com.zkys.generator.model.entity
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/10/9 10:17
 */
@Data
public class Column {

    /**
     * 所属表名
     */
    private String tableName;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 类型
     */
    private String dataType;

    /**
     * 备注
     */
    private String comment;

    /**
     * 列大小
     */
    private Integer columnSize;

    /**
     * 小数位数
     */
    private Integer decimalDigits;

    /**
     * 是否能为空
     */
    private Boolean nullAble;

    /**
     * 是否自增长
     */
    private Boolean autoIncrement;

    /**
     * 类型名
     */
    private String attributeName;

    private String uppercaseAttributeName;

    /**
     * 参数类型
     */
    private String attributeType;

    public void setColumnName(String columnName) {
        this.columnName = columnName;
        if (null != this.columnName) {
            this.uppercaseAttributeName = WordUtils.capitalizeFully(this.columnName.toLowerCase(), new char[]{'_'})
                    .replace("_", "");
            this.attributeName = StringUtils.uncapitalize(this.uppercaseAttributeName);
        }
    }
}
