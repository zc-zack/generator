package com.zkys.generator.enums;

import lombok.Data;

/**
 * com.zkys.generator.enums
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/10 15:45
 */
public enum  DataTypeEnum {

    CHAR("char", "String"),
    VARCHAR("varchar","String"),
    BLOB("blob","byte[]"),
    TEXT("text","String"),
    INTEGER("integer","Long"),
    TINYINT("tinyint","Integer"),
    SMALLINT("varchar","Integer"),
    MEDIUMINT("mediumint","Integer"),
    BIT("bit","Boolean"),
    BIGINT("varchar","BigInteger"),
    FLOAT("float","Float"),
    DOUBLE("double","Double"),
    DECIMAL("decimal","java.math.BigDecimal"),
    BOOLEAN("varchar","Integer"),
    DATE("date","Date"),
    DATETIME("datetime","java.util.Date"),
    INT("int","Integer"),
    TIMESTAMP("timestamp","java.util.Date");


    private final String mysqlDataType;

    private final String javaDataType;

    DataTypeEnum(String mysqlDataType, String javaDataType) {
        this.mysqlDataType = mysqlDataType;
        this.javaDataType = javaDataType;
    }
    public String getMysqlDataType() {
        return mysqlDataType;
    }

    public String getJavaDataType() {
        return javaDataType;
    }

    public static String getJavaDataType(String mysqlDataType) {
        for (DataTypeEnum e: DataTypeEnum.values()) {
            if (e.getMysqlDataType().equals(mysqlDataType)) {
                return e.getJavaDataType();
            }
        }
        return "";
    }

}
