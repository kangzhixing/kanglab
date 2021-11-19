package com.kang.lab.plugins.log;

import com.kang.lab.plugins.utils.PointCutUtil;
import com.kang.lab.plugins.validator.ValidatorAdvisorConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * 通用日志aop处理
 *
 * @author kangzhixing
 * @date 2021-03-19 20:58
 */
@Slf4j
@ConditionalOnExpression("!''.equals('${kanglab.log.packages:}')")
public class LogAdvisorConfig {

    @Value("${kanglab.log.packages:}")
    private String logPackages;

    @Bean
    @Order(1)
    public AspectJExpressionPointcutAdvisor logAdvisor() {
        log.info("[KangLabLog] 日志切面已开启");
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(PointCutUtil.buildPointCutPath(logPackages));
        advisor.setAdvice(new LogAdvice());
        return advisor;
    }

}