package com.kang.lab.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * String处理类
 *
 * @author kangzhixing
 * @date 2020-09-09 14:32
 */
@Slf4j
public final class SPelUtil {

    /**
     * 通过method拼装EvaluationContext上下文
     *
     * @param argNames 属性名数组
     * @param args 属性值数组
     * @return
     */
    public static EvaluationContext getEvaluationContextByArgs(String[] argNames, Object[] args) {
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < argNames.length; len++) {
            context.setVariable(argNames[len], args[len]);
        }
        return context;
    }

    /**
     * 通过方法拼装EvaluationContext上下文
     *
     * @param args
     * @param targetMethod
     * @return
     */
    public static EvaluationContext getEvaluationContextByMethod(Method targetMethod, Object[] args) {
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] argNames = discoverer.getParameterNames(targetMethod);
        return SPelUtil.getEvaluationContextByArgs(argNames, args);
    }

    /**
     * 解析spel表达式
     *
     * @param spel
     * @param context
     * @return
     */
    public static Object getObject(String spel, EvaluationContext context) {
        ExpressionParser parser = new SpelExpressionParser();
        try {
            Expression expression = parser.parseExpression(spel);
            return expression.getValue(context);
        } catch (Exception e) {
            log.warn("表达式解析错误");
            return null;
        }
    }

    /**
     * 从入参中取出所需参数集合
     *
     * @param context
     * @param spels
     * @return
     */
    public static List<Object> getObjects(String[] spels, EvaluationContext context) {
        List<Object> paramList = new ArrayList<>();
        if (spels != null && spels.length > 0) {
            for (String spel : spels) {
                // 解析spel表达式
                Object sp = getObject(spel, context);
                if (sp != null) {
                    paramList.add(sp);
                }
            }
        }
        return paramList;
    }
}
