package com.kang.lab.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author kangzhixing
 * @date 2020-09-09 13:34
 */

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    SUCCESS("00000", "成功"),
    ERROR_EXCEPTION("10000", "服务不可用,请重试！"),
    ERROR_INVALID_PARAM("20000", "缺少或非法的参数"),
    ERROR_FORBIDDEN("30000", "调用接口权限不足"),
    ERROR("40000", "业务处理失败");

    private final String code;
    private final String desc;
}