package com.kang.codetool.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetDbDataReq {

    @NotBlank(message = "连接字符串不能为空")
    private String connection;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "数据库类型不能为空")
    private String dbType;

    @NotBlank(message = "表名不能为空")
    private String tableName;

    private Integer pageNum;

    private Integer pageSize;

    private String condition;
}
