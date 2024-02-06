package com.kang.lab.plugins.validator.support;

import cn.hutool.core.lang.Validator;
import com.kang.lab.plugins.validator.OpenValidator;
import com.kang.lab.plugins.validator.annotations.URL;
import lombok.NonNull;

import javax.validation.ConstraintValidatorContext;

/**
 * URL校验
 */
public class URLValidator implements OpenValidator<URL, String> {
    /**
     * 是否必须
     */
    private boolean require = false;

    @Override
    public @NonNull Class<URL> annotationClass() {
        return URL.class;
    }

    /**
     * 传递注解参数,是否必须
     *
     * @param constraintAnnotation 手机号校验注解
     */
    @Override
    public void initialize(URL constraintAnnotation) {
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
        return Validator.isUrl(value);
    }


}
