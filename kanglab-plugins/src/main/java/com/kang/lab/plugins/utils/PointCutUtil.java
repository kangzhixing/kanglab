package com.jdt.open.capability.util;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * AOP切面工具类
 *
 * @author kangzhixing
 */
public class PointCutUtil {

    public static String getExpression(Class clazz) {
        return "@annotation(" + clazz.getName() + ")";
    }

    public static String getExpression(String[] packages) {
        return getPointCutExpression(packages, (packagePath) -> "execution(public * " + packagePath + "..*.*(..))");
    }


    public static String getExpression(Class clazz, String[] packages) {
        String expression = PointCutUtil.getExpression(clazz);
        String layeredExpression = PointCutUtil.getExpression(packages);
        if (StringUtils.isBlank(layeredExpression)) {
            return expression;
        }

        return expression + " || " + layeredExpression;
    }

    public static String getExpressionExcludeSubpackages(String[] packages) {
        return getPointCutExpression(packages, (packagePath) -> "execution(public * " + packagePath + ".*.*(..))");
    }

    private static String getPointCutExpression(String[] packages, Function<String, String> getExpression) {
        if (packages == null || packages.length == 0) {
            return "";
        }
        StringBuilder expression = new StringBuilder(getExpression.apply(packages[0]));
        if (packages.length > 1) {
            for (int i = 1; i < packages.length; i++) {
                if (packages[i].length() == 0) {
                    continue;
                }
                expression.append(" || ").append(getExpression.apply(packages[i]));
            }
        }
        return expression.toString();
    }
}
