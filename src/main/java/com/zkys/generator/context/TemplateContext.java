package com.zkys.generator.context;

import com.zkys.generator.common.constant.KeyConstant;
import com.zkys.generator.common.utils.ConfigUtil;
import com.zkys.generator.common.utils.SpringContextUtil;
import com.zkys.generator.common.utils.TemplateUtil;
import com.zkys.generator.model.Table;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * com.zkys.generator.context
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/11 9:42
 */
@Data
public class TemplateContext {

    private static Map<String, Object> systemVariables;

    private static Map<String, String> dynamicPathVariables;

    private Map<String, Object> templateVariables;

    private Table table;

    public static Builder newBuilder() {

        return new Builder();
    }

    public static class Builder {

        private Environment environment;
        private TemplateContext context;

        private Builder() {
            environment = SpringContextUtil.getBean(Environment.class);
            context = new TemplateContext();
            systemVariables = new HashMap<>();
            dynamicPathVariables = new HashMap<>();

            Properties properties = System.getProperties();
            systemVariables.put("config", ConfigUtil.class);
            systemVariables.put("utils", TemplateUtil.class);

            systemVariables.put("username", System.getenv("USERNAME"));
            systemVariables.put("computerName", System.getenv("COMPUTERNAME"));
            systemVariables.put("osName", properties.getProperty("os.name"));
            systemVariables.put("osArch", properties.getProperty("os.arch"));
            systemVariables.put("osVersion", properties.getProperty("os.version"));
        }

        public Builder buildTable(Table table) {
            if (null != table) {
                context.setTable(table);
                context.getTemplateVariables().put(KeyConstant.CLASS_NAME, table.getTableName());
                context.getTemplateVariables().put(KeyConstant.LOWERCASE_CLASS_NAME, table.getLowercaseClassName());
            }
            return this;
        }

        public Builder buildDynamicPath(Map<String, String> variables) {
            if (null != variables) {
                Map<String, String> cloneVariables = new HashMap<>();
                for (Map.Entry<String, String> entry : variables.entrySet()) {
                    cloneVariables.put(entry.getKey(), entry.getValue());
                }

                String className = cloneVariables.get(KeyConstant.CLASS_NAME);
                String lowercaseClassName = cloneVariables.get(KeyConstant.LOWERCASE_CLASS_NAME);

                if (org.apache.commons.lang3.StringUtils.isNotBlank(className)) {
                    cloneVariables.put(KeyConstant.LOWERCASE_CLASS_NAME, StringUtils.uncapitalize(className));
                } else if (StringUtils.isNotBlank(lowercaseClassName)) {
                    cloneVariables.put(KeyConstant.CLASS_NAME,
                            lowercaseClassName.substring(0, 1).toUpperCase() + lowercaseClassName.substring(1));
                }
                for (Map.Entry<String, String> entry : cloneVariables.entrySet()) {
                    dynamicPathVariables.put(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public Builder buildTemplate(Map<String, Object> variables) {
            context.setTemplateVariables(variables);
            return this;
        }

        public TemplateContext build() {
            return context;
        }


    }




}
