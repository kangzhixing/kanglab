package com.kang.lab.plugins.utils.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 敏感字段注解
 *
 * @author kangzhixing
 * @date 2023-04-24
 * @since 2.1.4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SensitiveField {

    /**
     * 脱敏类型
     */
    SensitiveType value() default SensitiveType.PASSWORD;

    /**
     * 前面保留位数
     */
    int front() default -1;

    /**
     * 后面保留位数
     */
    int end() default -1;
}
