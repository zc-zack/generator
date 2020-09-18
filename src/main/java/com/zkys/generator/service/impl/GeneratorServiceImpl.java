package com.zkys.generator.service.impl;

import com.zkys.generator.common.utils.SpringContextUtil;
import com.zkys.generator.context.TemplateContext;
import com.zkys.generator.model.Table;
import com.zkys.generator.model.TableItem;
import com.zkys.generator.service.GeneratorService;
import com.zkys.generator.table.MysqlTableService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * com.zkys.generator.service.impl
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/9/7 13:46
 */
@Log4j2
@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private MysqlTableService mysqlTableService;

    static {
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
    }

    @Value("${generator.template.output-paths:}")
    private String templateOutputPaths;

    @Value("${generator.template.base-path:}")
    private String templateBasePath;

    @Override
    public void generateZip(String[] tableNames, String path) {
        TableItem[] tableItems = new TableItem[tableNames.length];
        for (int i = 0; i < tableNames.length; i++) {
            tableItems[i] = new TableItem(tableNames[i]);
        }
        generateZip(tableItems, path);
    }

    @Override
    public void generateZip(TableItem[] tableItems, String zipPath) {
        try (FileOutputStream fos = new FileOutputStream(zipPath)){
            try (ZipOutputStream zos = new ZipOutputStream(fos)){
                for (TableItem tableItem : tableItems) {
                    Table table = mysqlTableService.getTale(tableItem.getTableName());
                    if (null == table) {
                        log.error("表[{}] 查询失败", tableItem.getTableName());
                        continue;
                    }
                    generateCode(TemplateContext.builder()
                            .buildTemplate(tableItem.getTemplateVariables())
                            .buildTable(table)
                            .buildDynamicPath(tableItem.getDynamicPathVariables())
                            .build(), zos);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void generateCode(TemplateContext templateContext, ZipOutputStream zos) {
        VelocityContext velocityContext = new VelocityContext(templateContext.buildMap());
        Map<String, String> outputPathMap = parseTemplateOutputPaths(templateContext);
        for (Map.Entry<String, String> entry : outputPathMap.entrySet()) {
            Template template  = Velocity.getTemplate(entry.getKey(), "UTF-8");
            StringWriter writer = new StringWriter();
            template.merge(velocityContext, writer);
            try {
                zos.putNextEntry(new ZipEntry(entry.getValue()));
                IOUtils.write(writer.toString(), zos, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> parseTemplateOutputPaths(TemplateContext context) {

        String[] rows = templateOutputPaths.split("\n");
        Map<String, String> outputPathMap = new HashMap<>();
        for (String row : rows) {
            int index = row.indexOf(":");
            if (-1 == index) {
                continue;
            }
            String fileName = row.substring(0, index).trim();
            try {
                String path = replace(row.substring(index + 1).trim(), context.getDynamicPathVariables());
                outputPathMap.put(templateBasePath + "/" + fileName, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outputPathMap;
    }

    private String replace(String pattern, Map<String, String> context) throws Exception {
        char[] patternChars = pattern.toCharArray();
        StringBuilder valueBuffer = new StringBuilder();
        StringBuilder variableNameBuffer = null;
        boolean inVariable = false;
        for (int i = 0; i < patternChars.length; ++i) {
            if (!inVariable && patternChars[i] == '{') {
                inVariable = true;
                variableNameBuffer = new StringBuilder();
                continue;
            }

            if (inVariable && patternChars[i] == '}') {
                inVariable = false;
                String variable = context.get(variableNameBuffer.toString());
                valueBuffer.append(valueBuffer == null? "null" : variable);
                variableNameBuffer = null;
                continue;
            }
            if (patternChars[i] == '\\' && ++i == patternChars.length) {
                throw new Exception("转义符 '\\' 后缺少字符");
            }
            StringBuilder activeBuffer = inVariable ? variableNameBuffer : valueBuffer;
            activeBuffer.append(patternChars[i]);
        }
        if (variableNameBuffer != null) {
            throw new Exception("结尾少了 }");
        }
        return valueBuffer.toString();
    }
}
