package com.kang.codetool.model;

import com.kang.framework.db.KlDatabaseType;
import com.kang.framework.db.KlFieldDescription;

import java.util.List;

public class CodeMakerGeneratCodeVO {


    /**
     * 字段列表
     */
    private List<KlFieldDescription> fieldDescriptions;

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

    public List<KlFieldDescription> getFieldDescriptions() {
        return fieldDescriptions;
    }

    public void setFieldDescriptions(List<KlFieldDescription> fieldDescriptions) {
        this.fieldDescriptions = fieldDescriptions;
    }

    public KlDatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(KlDatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassNameResult() {
        return className.startsWith("t_") ? className.substring(2) : className;
    }

    public void setClassNameResult(String classNameResult) {
        this.classNameResult = classNameResult;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
