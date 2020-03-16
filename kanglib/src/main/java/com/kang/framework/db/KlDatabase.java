package com.kang.framework.db;

import com.alibaba.druid.util.StringUtils;

import java.net.URLDecoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KlDatabase {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static int executeNonQuery(String connectionString, String sentence,
                                      HashMap<String, Object> parameters) throws Exception {
        Connection connection = null;
        KlNamedParameterStatement namedParameterStatement = null;
        try {
            connection = DriverManager.getConnection(connectionString);
            namedParameterStatement = new KlNamedParameterStatement(connection,
                    sentence);
            namedParameterStatement.setParameterStatementValue(parameters);
            return namedParameterStatement.executeUpdate();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }


    public static int executeNonQuery(String connectionString, String sentence)
            throws Exception {
        return executeNonQuery(connectionString, sentence, null);
    }


    public static Object executeScalar(String connectionString,
                                       String sentence, HashMap<String, Object> parameters)
            throws Exception {
        Connection connection = null;
        KlNamedParameterStatement namedParameterStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(connectionString);
            namedParameterStatement = new KlNamedParameterStatement(connection,
                    sentence);

            namedParameterStatement.setParameterStatementValue(parameters);
            resultSet = namedParameterStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getObject(1);
            } else {
                return null;
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static Object executeScalar(String connectionString, String sentence)
            throws Exception {
        return executeScalar(connectionString, sentence, null);
    }


    public static List fill(String connectionString, String sentence,
                            HashMap<String, Object> parameters) throws Exception {
        Connection connection = null;
        KlNamedParameterStatement namedParameterStatement;
        ResultSet resultSet;
        List result;
        try {
            connection = DriverManager.getConnection(connectionString);
            namedParameterStatement = new KlNamedParameterStatement(connection,
                    sentence);

            namedParameterStatement.setParameterStatementValue(parameters);
            resultSet = namedParameterStatement.executeQuery();
            result = resultSet2List(resultSet);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return result;
    }


    public static List fill(String connectionString, String sentence) throws Exception {
        return fill(connectionString, sentence, null);
    }


    public static List resultSet2List(ResultSet rs) throws SQLException {

        List list = new ArrayList();

        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();

        while (rs.next()) {
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("org.postgresql.Driver");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String sqlCommandKeywords = "and|exec|execute|insert|select|delete|update|count|chr|mid|master|"
            + "char|declare|sitename|net user|xp_cmdshell|or|create|drop|table|from|grant|use|group_concat|column_name|"
            + "information_schema.columns|table_schema|union|where|select|delete|update|orderhaving|having|by|count|*|truncate|like";

    private static final String sqlSeparatKeywords = "'|;|--|\'|\"|/*|%|#";

    private static final ArrayList<String> sqlKeywordsArray = new ArrayList<String>();


    static {
        String[] sqlSeparatKeywordsArray = sqlSeparatKeywords.split("\\|");
        for (String sqlSeparatKeyword : sqlSeparatKeywordsArray) {
            sqlKeywordsArray.add(sqlSeparatKeyword);
            sqlKeywordsArray.add(sqlSeparatKeyword + " ");
            sqlKeywordsArray.add(" " + sqlSeparatKeyword);
        }
    }

    /*
     * 验证SQL安全性
     *
     * @param String input
     *
     * @return boolean
     */
    public static boolean isSafetySql(String input) {
        if (StringUtils.isEmpty(input)) {
            return true;
        }
        input = URLDecoder.decode(input).toLowerCase();

        for (String sqlKeyword : sqlKeywordsArray) {
            if (input.indexOf(sqlKeyword) >= 0) {
                return false;
            }
        }
        return true;
    }

    /*
     * ذȫַ
     *
     * @param String input
     *
     * @return boolean
     */
    public static String getSafetySql(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        if (isSafetySql(input)) {
            return input;
        }
        input = URLDecoder.decode(input).toLowerCase();

        for (String sqlKeyword : sqlKeywordsArray) {
            if (input.indexOf(sqlKeyword) >= 0) {
                input = input.replace(sqlKeyword, "");
            }
        }
        return input;
    }
}
