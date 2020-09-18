package com.zkys.generator.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * com.zkys.generator.model
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/7 13:45
 */
@Data
public class TableItem {

    private String tableName;

    private Map<String, String> dynamicPathVariables = new HashMap<>();

    private Map<String, Object> templateVariables = new HashMap<>();


    public static Builder newBuilder() {
        return new Builder();
    }
    public TableItem(String tableName) {
        this.tableName = tableName;
    }

    public TableItem(String tableName, Map<String, String> dynamicPathVariables) {
        this.tableName = tableName;
        this.dynamicPathVariables = dynamicPathVariables;
    }

    public static class Builder {

        private TableItem item = new TableItem();

        public Builder tableName(String tableName) {
            item.setTableName(tableName);
            return this;
        }

        public Builder dynamicPathVariable(String key, String value) {
            item.getDynamicPathVariables().put(key, value);
            return this;
        }

        public Builder templateVariable(String key, Object value) {
            item.getTemplateVariables().put(key, value);
            return this;
        }

        public TableItem build() {
            return item;
        }
    }
}
