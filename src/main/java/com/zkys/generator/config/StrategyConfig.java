package com.zkys.generator.config;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zkys.generator.common.constant.ConstantValue;
import lombok.Data;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * com.zkys.generator.config
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/15 14:04
 */
@Data
public class StrategyConfig {

    private String[] superEntityColumns;

    /**
     * 自定义继承类全名，带包名
     */
    private String superEntityClass;

    private boolean entityLombok;

    /**
     * 自定义继承的Mapper类全称，带包名
     */
    private String superMapperClass = ConstantValue.SUPER_MAPPER_CLASS;

    /**
     * 自定义继承的Service类全称，带包名
     */
    private String supperServiceClass = ConstantValue.SUPER_SERVICE_CLASS;

    /**
     * 自定义继承的ServiceImpl类全称，带包名
     */
    private String supperServiceImpl = ConstantValue.SUPER_SERVICE_IMPL_CLASS;


    public StrategyConfig setSuperEntityClass(Class<?> clazz) {
        this.superEntityClass = clazz.getName();
        return this;
    }

    private void buildSuperEntityColumns (Class<?> clazz) {
        List<Field> fieldList = TableInfoHelper.getAllFields(clazz);
//        this.superEntityClass = fieldList.stream().map(field -> {
////            return StringUtils.camelToUnderline()
//            return Collections.EMPTY_LIST;
//        })

    }



}
