package com.kang.codetool.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * jar包工具类下载参数
 *
 * @author kangzhixing
 * @date 2021-12-02 16:01:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiInfo {

    /**
     * 接口名
     */
    private String apiName;

    /**
     * 接口名简称
     */
    private String apiSimpleName;

    /**
     * 方法列表
     */
    private List<MethodInfo> methodInfoList;
}