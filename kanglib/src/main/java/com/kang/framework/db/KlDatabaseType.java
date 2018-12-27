package com.kang.framework.db;

public enum KlDatabaseType {

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
     * @param code
     * @return
     */
    public static KlDatabaseType getByName(String code){
        for(KlDatabaseType e:KlDatabaseType.values()){
            if(code.equals(e.name())){
                return e;
            }
        }
        return  null;
    }
}
