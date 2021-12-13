package com.kang.codetool.vo.response;

import com.kang.lab.utils.StringUtil;
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
    private List<ParameterInfo> parameterTypeList;
    /**
     * 入参列表
     */
    private ParameterInfo returnType;

    /**
     * 返回类简称
     */
    private String returnTypeSimpleName;

    /**
     * 方法签名
     */
    private String methodSignature;
}