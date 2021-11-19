package com.kang.lab.plugins.log;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 动态advice
 * @author kangzhixing
 * @date 2021-03-19 17:45
 */
public class LogAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        LogInfo logInfo = LogUtil.logRequest(invocation);
        Object result = invocation.proceed();
        LogUtil.logResponse(logInfo, result);
        return result;
    }
}