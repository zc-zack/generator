package com.zkys.generator.service;

import com.zkys.generator.model.TableItem;

/**
 * com.zkys.generator.service
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/7 13:45
 */
public interface GeneratorService {

    /**
     * 反向生成压缩包
     * @param tableItems
     * @param zipPath
     */
    void generateZip(TableItem[] tableItems, String zipPath);

    void generateZip(String[] tableNames, String path);
}
