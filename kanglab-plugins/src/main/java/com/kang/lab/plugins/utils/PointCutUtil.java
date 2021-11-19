package com.kang.lab.plugins.utils;

public class PointCutUtil {

    public static String buildPointCutPath(String validatorPackages) {
        String[] packages = validatorPackages.split("\\|");
        StringBuilder expression = new StringBuilder();
        for (String packagePath : packages) {
            if (packagePath.length() == 0) {
                continue;
            }
            expression.append("execution(public * " + packagePath + "..*.*(..)) || ");
        }
        return expression.substring(0, expression.lastIndexOf(" || "));
    }
}
