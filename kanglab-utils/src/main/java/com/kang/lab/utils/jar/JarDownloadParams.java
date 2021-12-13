package com.kang.lab.utils.jar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * jar包工具类下载参数
 *
 * @author kangzhixing
 * @date 2021-12-02 16:01:34
 * @since 1.3.9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JarDownloadParams {

    /**
     * jar包在maven仓库中的groupId
     */
    private String groupId;
    /**
     * jar包在maven仓库中的artifactId
     */
    private String artifactId;
    /**
     * jar包在maven仓库中的version
     */
    private String version;
    /**
     * 远程maven仓库的URL地址，默认使用bw30的远程maven-public库
     */
    private String repository;
    /**
     * 下载的jar包存放的目标地址，例如./repo
     */
    private String target;
    /**
     * 登录远程maven仓库的用户名，若远程仓库不需要权限，设为null，默认为null
     */
    private String username;
    /**
     * 登录远程maven仓库的密码，若远程仓库不需要权限，设为null，默认为null
     */
    private String password;
}