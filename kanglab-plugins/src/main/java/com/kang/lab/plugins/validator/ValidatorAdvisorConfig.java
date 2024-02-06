package com.kang.lab.plugins.validator;


import com.jdt.open.capability.util.PointCutUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 通用参数校验处理
 *
 * @author kangzhixing
 * @date 2021-03-19 20:58
 */
@Slf4j
@ConditionalOnProperty(prefix = "open.validator", name = "enable", havingValue = "true")
public class ValidatorAdvisorConfig {

    @Value("${open.validator.packages:}")
    private String validatorPackages;

    @Bean
    @ConditionalOnMissingBean
    public HibernateValidatorConfiguration hibernateValidatorConfiguration() {
        return ValidatorUtil.getDefaultValidatorConfiguration();
    }

    @Bean
    public ValidatorUtil validatorUtil(HibernateValidatorConfiguration hibernateValidatorConfiguration) {
        return ValidatorUtil.getInstance(hibernateValidatorConfiguration);
    }

    @Bean
    public AspectJExpressionPointcutAdvisor annoValidatorAdviser(ValidatorUtil validatorUtil) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        if (StringUtils.isBlank(validatorPackages)) {
            advisor.setExpression(PointCutUtil.getExpression(OpenValid.class));
        } else {
            advisor.setExpression(PointCutUtil.getExpression(OpenValid.class, validatorPackages.split(",")));
        }
        advisor.setAdvice((MethodInterceptor) invocation -> {
            if (invocation.getArguments() != null) {
                for (Object arg : invocation.getArguments()) {
                    if (arg != null) {
                        validatorUtil.check(arg);
                    }
                }
            }
            return invocation.proceed();
        });
        advisor.setOrder(10);
        return advisor;
    }
}