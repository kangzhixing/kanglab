package com.kang.lab.plugins.log.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 打印日志
 * @date 2020-09-03
 * @author kangzhixing
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface KlLog {

    /**
     * 方法说明
     */
    String value() default "";
}
