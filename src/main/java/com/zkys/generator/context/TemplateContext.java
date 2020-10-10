package com.zkys.generator.context;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.zkys.generator.common.model.KeyConstant;
import com.zkys.generator.config.MysqlStrategyConfig;
import com.zkys.generator.model.entity.Table;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * com.zkys.generator.context
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/10/9 10:31
 */
@Data
public class TemplateContext {

    private Map<String, Object> map = new HashMap<>(16);

    private Map<String, String> dynamicPathVariables = new HashMap<>(8);

    public TemplateContext buildTable(Table table) {
        map.put("table", table);
        map.put("columnList", table.getColumnList());
        return this;
    }

    public TemplateContext buildGlobal(GlobalConfig config) {
        map.put("author", config.getAuthor());
        map.put("swagger2", config.isSwagger2());
        return this;
    }

    public TemplateContext buildPackage(PackageConfig config) {
        map.put("package", config.getParent());
        return this;
    }

    public TemplateContext buildStrategy(MysqlStrategyConfig config) {
        map.put("superEntityColumns", config.getSuperEntityColumns());
        map.put("superEntityClass", config.getSuperEntityClass());
        map.put("entityLombokModel", config.isEntityLombokModel());
        map.put("RestController", config.isRestControllerStyle());
        return this;
    }

    public TemplateContext buildDynamicVariables(Table table) {
        dynamicPathVariables.put(KeyConstant.PACKAGE_PATH, String.valueOf(map.get("package")).replace(".", "/"));
        dynamicPathVariables.put(KeyConstant.CLASS_NAME, table.getClassName());
        dynamicPathVariables.put(KeyConstant.LOWERCASE_CLASS_NAME, table.getLowercaseClassName());
        return this;
    }




}
