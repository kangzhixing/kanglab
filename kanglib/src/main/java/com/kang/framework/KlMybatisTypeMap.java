package com.kang.framework;

public class KlMybatisTypeMap {

    /**
     * 映射
     *
     * @param dbType     数据类型
     * @return JAVA类型
     */
    public static String map4MybatisPostgreSql(String dbType) {
        switch (dbType) {
            case "bigint":
                return "BIGINT";
            case "smallint":
                return "SMALLINT";
            case "integer":
                return "INTEGER";
            case "boolean":
                return "BIT";
            case "date":
            case "timestamp with time zone":
            case "timestamp without time zone":
                return "TIMESTAMP";
            case "numeric":
                return "NUMERIC";
            case "real":
                return "FLOAT";
            case "double precision":
                return "DOUBLE";
            default:
                return "VARCHAR";
        }
    }

    /**
     * 映射
     *
     * @param dbType 数据类型
     * @return JAVA类型
     */
    public static String map4Mybatis(String dbType) {
        switch (dbType) {
            case "bigint":
                return "bigint";
            case "bit":
                return "bit";
            case "int":
                return "integer";
            case "tinyint":
                return "tinyint";
            case "date":
                return "date";
            case "datetime":
            case "timestamp":
                return "timestamp";
            case "numeric":
                return "numeric";
            case "decimal":
                return "decimal";
            case "float":
            case "double":
                return "double";
            case "text":
            case "varchar":
                return "varchar";
            case "nvarchar":
                return "nvarchar";
            default:
                return "varchar";
        }
    }

}
