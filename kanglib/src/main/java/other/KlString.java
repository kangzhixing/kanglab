package other;

import com.alibaba.druid.util.StringUtils;

public class KlString {

    /// <summary>
    /// 首字母小写
    /// </summary>
    /// <param name="dbType">数据库类型</param>
    /// <param name="isNullable"></param>
    /// <returns>结果</returns>
    public static String toLowerFirst(String str) {
        if (StringUtils.isEmpty(str)) return str;
        if (str.length() == 1) return str.toLowerCase();
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /// <summary>
    /// 首字母大写
    /// </summary>
    /// <param name="dbType">数据库类型</param>
    /// <param name="isNullable"></param>
    /// <returns>结果</returns>
    public static String toUpperFirst(String str) {
        if (StringUtils.isEmpty(str)) return str;
        if (str.length() == 1) return str.toUpperCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /// <summary>
    /// 截取左半部分
    /// </summary>
    /// <param name="input">输入</param>
    /// <param name="length">长度</param>
    /// <param name="ex">扩展字符</param>
    /// <returns>结果</returns>
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

    /// <summary>
    /// 删除字符串中下划线
    /// </summary>
    /// <param name="srcStr">原字符串</param>
    /// <param name="org">删除字符</param>
    /// <param name="ob">替换字符</param>
    /// <returns>结果</returns>
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
}
