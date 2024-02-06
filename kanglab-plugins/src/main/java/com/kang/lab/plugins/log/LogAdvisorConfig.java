package com.kang.lab.plugins.log;

import com.kang.lab.plugins.log.annotation.NoLog;
import com.kang.lab.plugins.log.annotation.OpenLog;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import com.jdt.open.capability.util.PointCutUtil;

/**
 * 通用日志aop处理
 *
 * @author kangzhixing
 * @date 2021-03-19 20:58
 */
@Slf4j
@ConditionalOnProperty(prefix = "open.log", name = "enable", havingValue = "true")
public class LogAdvisorConfig {

    @Value("${open.log.packages:}")
    private String logPackages;

    @Bean
    public AspectJExpressionPointcutAdvisor logAdviser() {
        log.info("[OpenLog] 方法出入参日志切面已开启");
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        String expression;
        if (StringUtils.isBlank(logPackages)) {
            expression = PointCutUtil.getExpression(OpenLog.class);
        } else {
            expression = PointCutUtil.getExpression(OpenLog.class, logPackages.split(","));
        }
        expression = "(" + expression + ") && !" + PointCutUtil.getExpression(NoLog.class);
        advisor.setExpression(expression);
        advisor.setAdvice((MethodInterceptor) invocation -> {
            LogInfo logInfo = LogUtil.logRequest(invocation.getMethod(), invocation.getArguments());
            Object result = invocation.proceed();
            LogUtil.logResponse(logInfo, result);
            return result;
        });
        advisor.setOrder(2);
        return advisor;
    }
}