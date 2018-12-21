package com.kang.codetool.aop.anntion;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ViewPage {

    String description() default "";
}
