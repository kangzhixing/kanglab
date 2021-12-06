package com.kang.codetool.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
public class JarInfo {

    /**
     * jar包在maven仓库中的groupId
     */
    @NotBlank
    private String groupId;
    /**
     * jar包在maven仓库中的artifactId
     */
    @NotBlank
    private String artifactId;
    /**
     * jar包在maven仓库中的version
     */
    @NotBlank
    private String version;
}