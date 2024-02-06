package com.kang.lab.plugins.validator.annotations;


import com.kang.lab.plugins.dto.ICodeEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * 校验属性是否在枚举范围内
 *
 * @author wangjing921
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Documented
@ReportAsSingleViolation
@Constraint(validatedBy = {})
public @interface ByEnum {
    /**
     * 接受的枚举
     */
    Class<? extends ICodeEnum<?>> enumClass();

    /**
     * 默认异常msg
     */
    String message() default "意外的枚举类型";

    /**
     * 指定分组
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
