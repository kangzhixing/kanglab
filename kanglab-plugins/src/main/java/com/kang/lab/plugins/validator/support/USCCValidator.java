package com.kang.lab.plugins.validator.support;

import cn.hutool.core.util.CreditCodeUtil;
import com.kang.lab.plugins.validator.OpenValidator;
import com.kang.lab.plugins.validator.annotations.USCC;
import lombok.NonNull;

import javax.validation.ConstraintValidatorContext;

/**
 * 统一社会信用代码校验
 */
public class USCCValidator implements OpenValidator<USCC,String> {
    /**
     * 是否必须
     */
    private boolean require = false;
    @Override
    public @NonNull Class<USCC> annotationClass() {
        return USCC.class;
    }

    /**
     * 传递注解参数,是否必须
     * @param constraintAnnotation 手机号校验注解
     */
    @Override
    public void initialize(USCC constraintAnnotation) {
        require = constraintAnnotation.required();
    }

    /**
     * 判断是否通过
     * @param value 参数
     * @param context 上下文
     * @return 是否通过
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value==null && !require){
            return true;
        }
        return CreditCodeUtil.isCreditCode(value);
    }


}
