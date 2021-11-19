package com.kang.lab.utils.enums;

public enum DatabaseTypeEnum {

    /**
     * OleDb数据库
     */
    OleDb,

    /**
     * SqlServer数据库
     */
    SqlServer,

    /**
     * SQLite数据库
     */
    SQLite,

    /**
     * MySql数据库
     */
    MySql,

    /**
     * PostgreSql数据库
     */
    PostgreSql;

    /**
     * 根据code获取去value
     *
     * @param code
     * @return
     */
    public static DatabaseTypeEnum getByName(String code) {
        if (code == null) {
            return null;
        }
        for (DatabaseTypeEnum e : DatabaseTypeEnum.values()) {
            if (code.equalsIgnoreCase(e.name())) {
                return e;
            }
        }
        return null;
    }
}
