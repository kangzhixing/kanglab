package com.kang.codetool.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
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
public class MethodInfo {

    /**
     * 方法名
     */
    private String methodName;
    /**
     * 入参列表
     */
    private List<String> parameterTypeList;
    /**
     * 返回类
     */
    private String returnType;
}