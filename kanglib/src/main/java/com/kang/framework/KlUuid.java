package com.kang.framework;

import com.alibaba.druid.util.StringUtils;

import java.util.UUID;

public class KlUuid {

    /**
     * 获取新的UUID
     * @return 结果
     */
    public static String newUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
