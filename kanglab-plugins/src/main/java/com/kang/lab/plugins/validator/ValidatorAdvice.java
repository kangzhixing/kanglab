package com.kang.lab.plugins.validator;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Function;

/**
 * 动态advice
 * @author kangzhixing
 * @date 2021-04-07 11:30
 */
public class ValidatorAdvice implements MethodInterceptor {

    private Function<String, Object> resultHandle;

    public ValidatorAdvice(Function<String, Object> resultHandle){
        this.resultHandle = resultHandle;
    }

    /**
     * 校验器
     */
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Set<ConstraintViolation<Object>> violations;
        for (Object arg : invocation.getArguments()) {
            violations = VALIDATOR.validate(arg);
            // 如果该项校验未通过，则返回校验信息
            if (!CollectionUtils.isEmpty(violations)) {
                return resultHandle.apply(combineValidationMessage(violations));
            }
        }
        return invocation.proceed();
    }

    /**
     * 组装校验结果信息
     *
     * @param violations
     * @return
     */
    private String combineValidationMessage(Set<ConstraintViolation<Object>> violations) {
        ArrayList<String> list = Lists.newArrayList();
        for (ConstraintViolation<Object> violation : violations) {
            list.add(violation.getMessage());
        }
        return Joiner.on(";").join(list);
    }
}