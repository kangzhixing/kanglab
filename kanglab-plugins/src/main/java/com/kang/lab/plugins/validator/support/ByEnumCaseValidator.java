package com.kang.lab.plugins.validator.support;

import com.kang.lab.plugins.dto.ICodeEnum;
import com.kang.lab.plugins.utils.EnumUtil;
import com.kang.lab.plugins.validator.OpenValidator;
import com.kang.lab.plugins.validator.annotations.ByEnum;
import lombok.NonNull;

import javax.validation.ConstraintValidatorContext;

/**
 * 校验属性是否在枚举范围内
 *
 * @author wangjing921
 */
public class ByEnumCaseValidator implements OpenValidator<ByEnum, Object> {
    /**
     * 接受的枚举值
     */
    private Class<? extends ICodeEnum<?>> enumClass;

    @Override
    public @NonNull Class<ByEnum> annotationClass() {
        return ByEnum.class;
    }

    @Override
    public void initialize(ByEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    /**
     * 判断是否通过
     *
     * @param value   参数
     * @param context 上下文
     * @return 是否通过
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return EnumUtil.getEnumByCode(enumClass, value) != null;
    }


}
