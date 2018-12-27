package com.kang.framework;

import com.alibaba.druid.util.StringUtils;

public class KlString {

    /**
     * 首字母小写
     * @param str 数据库类型
     * @return 结果
     */
    public static String toLowerFirst(String str) {
        if (StringUtils.isEmpty(str)) return str;
        if (str.length() == 1) return str.toLowerCase();
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 首字母大写
     * @param str 数据库类型
     * @return 结果
     */
    public static String toUpperFirst(String str) {
        if (StringUtils.isEmpty(str)) return str;
        if (str.length() == 1) return str.toUpperCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 截取左半部分
     * @param input 输入
     * @param length 长度
     * @param ex 扩展字符
     * @return 结果
     */
    public static String left(String input, int length, String ex) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        if (input.length() > length) {
            return input.substring(0, length) + (StringUtils.isEmpty(ex) ? "..." : ex);
        } else {
            return input;
        }
    }

    /**
     * 删除字符串中下划线
     * @param srcStr 原字符串
     * @param org 删除字符
     * @param ob 替换字符
     * @return 结果
     */
    public static String replaceUnderline(String srcStr, String org, String ob) {
        String newString = "";
        int first;
        org = (StringUtils.isEmpty(org) ? "_" : org);
        ob = (ob == null ? "" : ob);
        while (srcStr.indexOf(org) != -1) {
            first = srcStr.indexOf(org);
            if (first != srcStr.length()) {
                newString = newString + srcStr.substring(0, first) + ob;
                srcStr = srcStr.substring(first + org.length());
                srcStr = toUpperFirst(srcStr);
            }
        }
        newString = newString + srcStr;
        return newString;
    }

    public static String replaceUnderline(String srcStr) {
        return replaceUnderline(srcStr, "_", "");
    }

    public static boolean isBlank(String description) {
        return description == null || description.length() == 0;
    }

    public static String format(String content, Object... strArr) {
        if (strArr == null || strArr.length == 0) {
            return content;
        }
        for (int i = 0; i < strArr.length; i++) {
            content = content.replaceAll("\\{" + i + "}", strArr[i] == null ? "" : strArr[i].toString());
        }
        return content;
    }
}
