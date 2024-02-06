package com.kang.lab.plugins.validator.annotations;


import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * 手机号校验
 *
 * @author wangjing921
 * @version 1.0
 * @date 2021/12/20 10:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Documented
@ReportAsSingleViolation
@Constraint(validatedBy = {})
public @interface Phone {
    /**
     * 可以理解为是否必须
     */
    boolean required() default false;

    /**
     * 默认异常msg
     */
    String message() default "手机号码格式不正确";

    /**
     * 指定分组
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
