package com.kang.lab.plugins.validator;

import lombok.NonNull;

import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;

public interface OpenValidator<A extends Annotation,V> extends ConstraintValidator<A,V> {
    @NonNull Class<A> annotationClass();
}
