package com.kang.lab.utils.db;


import com.kang.lab.utils.enums.DatabaseTypeEnum;

public class DbTypeMapUtil {

    public static String map4J(String dbType, Boolean isNullable) {
        return map4J(dbType, isNullable, DatabaseTypeEnum.MySql);
    }

    public static String map4J(String dbType, Boolean isNullable, DatabaseTypeEnum databaseType) {
        isNullable = isNullable == null ? false : isNullable;
        databaseType = databaseType == null ? DatabaseTypeEnum.MySql : databaseType;
        switch (databaseType) {
            case MySql:
                return map4JMysql(dbType, isNullable);

            case PostgreSql:
                return map4JPostgreSql(dbType, isNullable);

            default:
                return map4JMysql(dbType, isNullable);
        }

    }

    /**
     * 映射
     *
     * @param dbType     数据类型
     * @param isNullable
     * @return JAVA类型
     */
    private static String map4JMysql(String dbType, Boolean isNullable) {
        switch (dbType) {
            case "bigint":
                return isNullable ? "Long" : "long";
            case "int":
            case "smallint":
            case "tinyint":
                return isNullable ? "Integer" : "int";
            case "bit":
                return isNullable ? "Boolean" : "boolean";
            case "smalldatetime":
            case "date":
            case "datetime":
            case "timestamp":
                return "Date";
            case "numeric":
            case "decimal":
                return "BigDecimal";
            case "float":
                return isNullable ? "Double" : "double";
            default:
                return "String";
        }
    }

    /**
     * 映射
     *
     * @param dbType     数据类型
     * @param isNullable
     * @return JAVA类型
     */
    private static String map4JPostgreSql(String dbType, Boolean isNullable) {
        switch (dbType) {
            case "bigint":
                return isNullable ? "Long" : "long";
            case "smallint":
            case "integer":
                return isNullable ? "Integer" : "int";
            case "boolean":
                return isNullable ? "Boolean" : "boolean";
            case "date":
            case "timestamp":
            case "timestamp with time zone":
            case "timestamp without time zone":
                return "Date";
            case "numeric":
                return "BigDecimal";
            case "real":
                return isNullable ? "Float" : "float";
            case "double precision":
                return isNullable ? "Double" : "double";
            default:
                return "String";
        }
    }


    /**
     * 映射
     *
     * @param dbType     数据类型
     * @param isNullable
     * @return JAVA类型
     */
    public static String map4CSharp(String dbType, Boolean isNullable) {
        isNullable = isNullable == null ? false : isNullable;
        switch (dbType) {
            case "bigint":
                return isNullable ? "long?" : "long";
            case "int":
            case "tinyint":
                return isNullable ? "int?" : "int";
            case "bit":
                return isNullable ? "byte?" : "byte";
            case "smalldatetime":
            case "date":
            case "datetime":
            case "timestamp":
                return isNullable ? "DateTime?" : "DateTime";
            case "decimal":
            case "float":
                return isNullable ? "decimal?" : "decimal";
            default:
                return "String";
        }
    }
}
