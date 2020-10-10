package com.zkys.generator.config;

import com.zkys.generator.common.model.ConstantValue;
import lombok.Data;

/**
 * com.zkys.generator.config
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/10/10 14:36
 */
@Data
public class PackageConfig {

    private String parent;

    private String moduleName;

    private String entity;

    private String service;

    private String serviceImpl;

    private String controller;

    private String controllerApi;

    private String mapper;

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
        this.parent = this.parent + ConstantValue.DOT + moduleName;
    }

    public void setEntity(String entity) {
        this.entity = this.parent + ConstantValue.DOT + entity;
    }

    public void setService(String service) {
        this.service = this.parent + ConstantValue.DOT + service;
    }

    public void setServiceImpl(String serviceImpl) {
        this.serviceImpl = this.parent + ConstantValue.DOT + serviceImpl;
    }

    public void setController(String controller) {
        this.controller = this.parent + ConstantValue.DOT + controller;
    }

    public void setControllerApi(String controllerApi) {
        this.controllerApi = this.parent + ConstantValue.DOT + controllerApi;
    }

    public void setMapper(String mapper) {
        this.mapper = this.parent + ConstantValue.DOT + mapper;
    }
}
