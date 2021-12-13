package com.kang.lab.utils;

import org.mapstruct.IterableMapping;
import org.mapstruct.MapperConfig;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author kangzhixing
 * @date 2021-12-03 18:43:44
 */
@MapperConfig
public interface BaseConverter <SOURCE, TARGET> {
    /**
     * 映射同名属性
     */
    @Named(value="sourceToTarget")
    TARGET sourceToTarget(SOURCE var1);

    /**
     * 反向，映射同名属性
     */
    @Named(value="targetToSource")
    SOURCE targetToSource(TARGET var1);

    /**
     * 映射同名属性，集合形式
     */
    @IterableMapping(qualifiedByName = "sourceToTarget")
    List<TARGET> sourceToTarget(List<SOURCE> var1);

    /**
     * 反向，映射同名属性，集合形式
     */
    @IterableMapping(qualifiedByName = "targetToSource")
    List<SOURCE> targetToSource(List<TARGET> var1);

    /**
     * 映射同名属性，集合流形式
     */
    @IterableMapping(qualifiedByName = "sourceToTarget")
    List<TARGET> sourceToTarget(Stream<SOURCE> stream);

    /**
     * 反向，映射同名属性，集合流形式
     */
    @IterableMapping(qualifiedByName = "targetToSource")
    List<SOURCE> targetToSource(Stream<TARGET> stream);
}