package com.kang.lab.plugins.validator.annotations;


import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * URL校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Documented
@ReportAsSingleViolation
@Constraint(validatedBy = {})
public @interface URL {
    /**
     * 可以理解为是否必须
     */
    boolean required() default false;

    /**
     * 默认异常msg
     */
    String message() default "URL格式不正确";

    /**
     * 指定分组
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
