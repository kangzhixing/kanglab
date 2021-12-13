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
public class ApiListRequest {

    /**
     * 依赖信息
     */
    @NotBlank
    private String dependencies;
    /**
     * 远程maven仓库的URL地址，默认使用远程maven-public库
     */
    private String repository;
    /**
     * 登录远程maven仓库的用户名，若远程仓库不需要权限，设为null，默认为null
     */
    private String username;
    /**
     * 登录远程maven仓库的密码，若远程仓库不需要权限，设为null，默认为null
     */
    private String password;
}