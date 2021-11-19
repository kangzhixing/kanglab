package com.kang.lab.utils;

import java.util.UUID;

public class UUIDUtil {

    /**
     * 获取新的UUID
     * @return 结果
     */
    public static String newUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
