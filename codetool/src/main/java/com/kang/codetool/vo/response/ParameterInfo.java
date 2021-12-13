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
public class ParameterInfo {

    /**
     * 属性名
     */
    private String parameterName;
    /**
     * 类名
     */
    private String className;
    /**
     * 类名简称
     */
    private String simpleClassName;
    /**
     * 类名简称
     * {@link ParameterTypeEnum}
     */
    private String type;
    /**
     * 返回类简称
     */
    private List<ParameterInfo> childParamList;
}