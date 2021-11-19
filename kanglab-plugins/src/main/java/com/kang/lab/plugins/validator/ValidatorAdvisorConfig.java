package com.kang.lab.plugins.validator;

import com.kang.lab.plugins.utils.PointCutUtil;
import com.kang.lab.utils.enums.ResponseCodeEnum;
import com.kang.lab.utils.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;

/**
 * 通用参数校验处理
 *
 * @author kangzhixing
 * @date 2021-03-19 20:58
 */
@Slf4j
@ConditionalOnExpression("!''.equals('${kanglab.validator.packages:}')")
public class ValidatorAdvisorConfig {

    @Value("${kanglab.validator.packages:}")
    private String validatorPackages;

    @Bean
    public AspectJExpressionPointcutAdvisor controllerValidatorAdvisor() {
        log.info("[KangLabValidator] 参数校验切面已开启");
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(PointCutUtil.buildPointCutPath(validatorPackages));
        advisor.setAdvice(new ValidatorAdvice((msg) -> RestResponse.error(ResponseCodeEnum.ERROR_INVALID_PARAM, msg)));
        return advisor;
    }
}