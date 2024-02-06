package com.kang.lab.plugins.dto;

/**
 * 枚举定义
 *
 * @author kangzhixing
 * @date 2022-04-14
 */
public interface ICodeEnum<T> {

    T getCode();

    String getMsg();
}
