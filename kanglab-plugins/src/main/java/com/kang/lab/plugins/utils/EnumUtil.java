package com.kang.lab.plugins.utils;


import com.kang.lab.plugins.dto.ICodeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ext.liuzhiliang1
 * @ClassName EnumUtil
 * @Description 根据枚举中任意参数获取枚举类
 * @Date 2021/11/8 21:24
 **/
public class EnumUtil {

    /**
     * 返回指定编码的'枚举'
     *
     * @return StatusTransitionEnum
     */
    public static <T extends ICodeEnum> T getEnumByCode(Class<T> clazz, Object code) {
        for (T aEnum : clazz.getEnumConstants()) {
            if (aEnum.getCode().equals(code)) {
                return aEnum;
            }
        }
        return null;
    }

    /**
     * 返回指定名称的'枚举'
     *
     * @return StatusTransitionEnum
     */
    public static <T extends ICodeEnum<?>> T getEnumByMsg(Class<T> clazz, String msg) {
        for (T aEnum : clazz.getEnumConstants()) {
            if (aEnum.getMsg().equals(msg)) {
                return aEnum;
            }
        }
        return null;
    }

    /**
     * 将枚举code转为code列表
     *
     * @param <T> 传入枚举类型
     * @param <V> 待输出List<V>类型
     */
    public static <T extends ICodeEnum<V>, V> List<V> getEnumCodeList(Class<T> clazz) {
        return Arrays.stream(clazz.getEnumConstants()).map(ICodeEnum::getCode).collect(Collectors.toList());
    }

    /**
     * 获取枚举的code
     *
     * @return 枚举为空时返回null
     */
    public static <R> R getCode(ICodeEnum<R> baseEnum) {
        return Optional.ofNullable(baseEnum)
                .map(ICodeEnum::getCode)
                .orElse(null);
    }

    /**
     * 获取枚举的msg
     *
     * @return 枚举为空时返回null
     */
    public static String getMsg(ICodeEnum<?> baseEnum) {
        return Optional.ofNullable(baseEnum)
                .map(ICodeEnum::getMsg)
                .orElse(null);
    }
}
