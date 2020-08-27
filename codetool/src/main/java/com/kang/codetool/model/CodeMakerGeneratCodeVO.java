package com.kang.codetool.model;

import com.kang.framework.db.KlDatabaseType;
import com.kang.framework.db.KlFieldDescription;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CodeMakerGeneratCodeVO {

    /**
     * 字段列表
     */
    private List<KlFieldDescription> fieldDescriptions = new ArrayList();

    /**
     * 数据库类型
     */
    private KlDatabaseType databaseType;


    /**
     * 数据库链接
     */
    private String connectionString;

    /**
     * 表或视图名
     */
    private String table;

    /**
     * 包
     */
    private String packagePath;

    /**
     * 类名
     */
    private String className;

    /**
     * 类名
     */
    private String classNameResult;

    /**
     * 类型
     */
    private String type;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 文件扩展名
     */
    private String extension;

    public String getClassNameResult() {
        return className.startsWith("t_") ? className.substring(2) : className;
    }

    public KlFieldDescription tryToGetPrimaryKey() {
        KlFieldDescription field;
        if (this.getFieldDescriptions().stream().anyMatch(f -> "PRI".equals(f.getColumnKey()))) {
            field = this.getFieldDescriptions().stream().filter(f -> "PRI".equals(f.getColumnKey())).findFirst().orElseGet(null);
        } else {
            field = this.getFieldDescriptions().stream().findFirst().orElseGet(null);
        }
        return field;
    }
}
