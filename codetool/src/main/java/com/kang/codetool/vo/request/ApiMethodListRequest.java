package com.kang.codetool.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * api中方法列表参数
 *
 * @author kangzhixing
 * @date 2021-12-02 16:01:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiMethodListRequest {

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
    /**
     * 远程maven仓库的URL地址，默认使用bw30的远程maven-public库
     */
    @NotBlank
    private String repository;
    /**
     * 登录远程maven仓库的用户名，若远程仓库不需要权限，设为null，默认为null
     */
    @NotBlank
    private String username;
    /**
     * 登录远程maven仓库的密码，若远程仓库不需要权限，设为null，默认为null
     */
    @NotBlank
    private String password;
    /**
     * 接口名
     */
    @NotBlank
    private String interfaceName;
}