package com.zkys.generator.context;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.zkys.generator.common.model.KeyConstant;
import com.zkys.generator.config.MysqlStrategyConfig;
import com.zkys.generator.model.entity.Table;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.*;

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

    private Set<String> importPackages = new HashSet<>();

    public TemplateContext buildTable(Table table) {
        map.put("table", table);
        map.put("entity", table.getClassName());
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
        System.out.println(Arrays.toString(config.getSuperEntityColumns()));
        map.put("superEntityClass", getSuperEntityClass(config.getSuperEntityClass()));
        map.put("entityLombokModel", config.isEntityLombokModel());
        map.put("RestController", config.isRestControllerStyle());
        map.put("importPackages", importPackages);
        return this;
    }

    public TemplateContext buildDynamicVariables(Table table) {
        dynamicPathVariables.put(KeyConstant.PACKAGE_PATH, String.valueOf(map.get("package")).replace(".", "/"));
        dynamicPathVariables.put(KeyConstant.CLASS_NAME, table.getClassName());
        dynamicPathVariables.put(KeyConstant.LOWERCASE_CLASS_NAME, table.getLowercaseClassName());
        return this;
    }

    private String getSuperEntityClass(String classPath) {
        if (!StringUtils.isNotBlank(classPath)){
            return null;
        }
        addImportPackages(classPath.substring(0, classPath.lastIndexOf(StringPool.DOT) -1));
        return classPath.substring(classPath.lastIndexOf(StringPool.DOT) + 1);
    }

    private void addImportPackages(String path) {
        importPackages.add(path);
    }




}
