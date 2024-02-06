package com.kang.lab.plugins.validator.support;


import cn.hutool.core.lang.Validator;
import com.kang.lab.plugins.validator.OpenValidator;
import com.kang.lab.plugins.validator.annotations.CertNo;
import lombok.NonNull;

import javax.validation.ConstraintValidatorContext;

/**
 * 证件号校验
 */
public class CertNoValidator implements OpenValidator<CertNo, String> {
    /**
     * 是否必须
     */
    private boolean require = false;

    @Override
    public @NonNull Class<CertNo> annotationClass() {
        return CertNo.class;
    }
    @Override
    public void initialize(CertNo constraintAnnotation) {
        require = constraintAnnotation.required();
    }

    /**
     * 判断是否通过
     *
     * @param value   参数
     * @param context 上下文
     * @return 是否通过
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null && !require) {
            return true;
        }
        return value == null || Validator.isCitizenId(value);
    }


}
