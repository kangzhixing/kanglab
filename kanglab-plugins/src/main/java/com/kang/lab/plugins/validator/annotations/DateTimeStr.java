package com.kang.lab.plugins.validator.annotations;


import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * 字符串时间格式校验器
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Documented
@ReportAsSingleViolation
@Constraint(validatedBy = {})
public @interface DateTimeStr {
    /**
     * 可以理解为是否必须
     */
    boolean required() default false;
    String message() default "时间格式不正确";
    String format();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
