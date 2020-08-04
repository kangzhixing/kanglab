package com.kang.framework.db;

import com.kang.framework.KlString;
import lombok.Data;

@Data
public class KlFieldDescription {

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否可为Null
     */
    private Boolean isNullable;

    /**
     * 是否为自增列
     */
    private Boolean isIdentity;

    /**
     * 主外键情况
     */
    private String columnKey;

    /**
     * 长度
     */
    private Integer length;

    /**
     * 字段名
     */
    private String name;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段名(去除下划线)
     */
    private String simpleName;

    public String getSimpleName() {
        return KlString.replaceUnderline(name);
    }
}