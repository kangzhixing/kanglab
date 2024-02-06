package com.kang.lab.plugins.validator.support;


import cn.hutool.core.date.DateUtil;
import com.kang.lab.plugins.validator.OpenValidator;
import com.kang.lab.plugins.validator.annotations.DateTimeStr;
import lombok.NonNull;

import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

/**
 * 时间格式校验器
 */
public class DateTimeStrValidator implements OpenValidator<DateTimeStr,String> {

    private DateTimeStr dateTimeStr;
    private boolean require = false;
    @Override
    public @NonNull Class<DateTimeStr> annotationClass() {
        return DateTimeStr.class;
    }

    @Override
    public void initialize(DateTimeStr dateTimeStr) {
        this.dateTimeStr=dateTimeStr;
        this.require=dateTimeStr.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null && !require) {
            return true;
        }
        try {
            SimpleDateFormat format = DateUtil.newSimpleFormat(dateTimeStr.format());
            format.parse(value);
            return true;
        } catch (Exception e){
            return false;
        }
    }


}
