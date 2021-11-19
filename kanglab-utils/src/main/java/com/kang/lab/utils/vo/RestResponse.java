package com.kang.lab.utils.vo;

import com.kang.lab.utils.enums.ResponseCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Restful接口通用返回类
 *
 * @author kangzhixing
 */
@Data
@ApiModel("http响应结果")
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("返回响应码")
    private String code;

    @ApiModelProperty("返回信息")
    private String msg;

    @ApiModelProperty("返回结果")
    private T data;

    private RestResponse(ResponseCodeEnum error, String msg, T data) {
        this.code = error.getCode();
        this.msg = msg;
        this.data = data;
    }

    private RestResponse(ResponseCodeEnum codeEnum) {
        this(codeEnum, codeEnum.getDesc(), null);
    }

    private RestResponse(ResponseCodeEnum codeEnum, String msg) {
        this(codeEnum, msg, null);
    }

    private RestResponse(ResponseCodeEnum codeEnum, T data) {
        this(codeEnum.getCode(), codeEnum.getDesc(), data);
    }

    public static <T> RestResponse<T> success() {
        return new RestResponse(ResponseCodeEnum.SUCCESS);
    }

    public static <T> RestResponse<T> success(T data) {
        return new RestResponse(ResponseCodeEnum.SUCCESS,  data);
    }

    public static <T> RestResponse<T> error(ResponseCodeEnum codeEnum, String msg) {
        return new RestResponse(codeEnum.getCode(), msg, null);
    }

    public static <T> RestResponse<T> error(String code, String msg) {
        return new RestResponse(code, msg, null);
    }

    public static <T> RestResponse<T> error(String code, String msg, T data) {
        return new RestResponse(code, msg, data);
    }

    public Boolean isSuccess() {
        return ResponseCodeEnum.SUCCESS.getCode().equals(this.code);
    }
}
