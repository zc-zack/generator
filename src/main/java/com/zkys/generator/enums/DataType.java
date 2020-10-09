package com.zkys.generator.enums;

/**
 * com.zkys.generator.enums
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/10/9 10:34
 */
public enum DataType {
    CHAR("char","String"),
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

    private final String mySqlDataType;
    private final String javaDataType;

    DataType(String mySqlDataType, String javaDataType) {
        this.mySqlDataType = mySqlDataType;
        this.javaDataType = javaDataType;
    }
}
