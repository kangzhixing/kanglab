package com.kang.lab.plugins.validator;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.kang.lab.plugins.validator.support.*;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import java.util.*;

/**
 * @author kangzhixing
 */
public class ValidatorUtil implements Validator {

    private static final List<OpenValidator> DEFAULT_VALIDATORS = Arrays.asList(
            new CertNoValidator(),
            new DateTimeStrValidator(),
            new PhoneValidator(),
            new URLValidator(),
            new USCCValidator(),
            new ByEnumCaseValidator()
    );
    
    public static HibernateValidatorConfiguration getDefaultValidatorConfiguration() {
        return Validation.byProvider(HibernateValidator.class).configure();
    }

    private ValidatorUtil(Validator validator) {
        this.validator = validator;
    }

    public static ValidatorUtil getInstance() {
        return ValidatorUtil.getInstance(getDefaultValidatorConfiguration(), DEFAULT_VALIDATORS);
    }

    public static ValidatorUtil getInstance(HibernateValidatorConfiguration hibernateValidatorConfiguration) {
        return ValidatorUtil.getInstance(hibernateValidatorConfiguration, DEFAULT_VALIDATORS);
    }

    @SuppressWarnings("unchecked")
    private static ValidatorUtil getInstance(HibernateValidatorConfiguration hibernateValidatorConfiguration, List<OpenValidator> openValidators) {
        HibernateValidatorConfiguration configuration = Optional.ofNullable(hibernateValidatorConfiguration)
                .orElse(getDefaultValidatorConfiguration());
        if (openValidators != null) {
            for (OpenValidator openValidator : openValidators) {
                ConstraintMapping mapping = hibernateValidatorConfiguration.createConstraintMapping();
                mapping.constraintDefinition(openValidator.annotationClass()).validatedBy(openValidator.getClass());
                configuration = configuration.addMapping(mapping);
            }
        }
        return new ValidatorUtil(configuration.buildValidatorFactory().getValidator());
    }


    /**
     * 校验器
     */
    private final Validator validator;

    public <T> void check(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        // 如果该项校验未通过，则返回校验信息
        if (!CollectionUtils.isEmpty(violations)) {
            throw new ConstraintViolationException(violations);
        }
    }

    public <T> String checkResult(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        // 如果该项校验未通过，则返回校验信息
        if (!CollectionUtils.isEmpty(violations)) {
            return combineValidationMessage(violations);
        }
        return null;
    }

    /**
     * 组装校验结果信息
     *
     * @param violations
     * @return
     */
    public static <T extends ConstraintViolation> String combineValidationMessage(Set<T> violations) {
        ArrayList<String> list = Lists.newArrayList();
        for (ConstraintViolation violation : violations) {
            list.add(violation.getMessage());
        }
        return Joiner.on(";").join(list);
    }

    /**
     * 返回单个校验结果信息
     *
     * @param violations
     * @return
     */
    public static <T extends ConstraintViolation> String singleMessage(Set<T> violations) {
        Iterator<T> iterator = violations.iterator();
        return iterator.next().getMessage();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return validator.validate(object, groups);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
        return validator.validateProperty(object, propertyName, groups);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        return validator.validateValue(beanType, propertyName, value, groups);
    }

    @Override
    public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
        return validator.getConstraintsForClass(clazz);
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return validator.unwrap(type);
    }

    @Override
    public ExecutableValidator forExecutables() {
        return validator.forExecutables();
    }
}
