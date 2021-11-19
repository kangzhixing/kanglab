package com.kang.lab.utils.exception;

import com.kang.lab.utils.enums.ResponseCodeEnum;
import lombok.Data;

/**
 * 业务异常
 */
@Data
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 5734560807293846708L;

    /**
     * 系统错误码
     */
    private String code;

    /**
     * 错误描述
     */
    private String msg;

    /**
     * 数据
     */
    private Object data;

    public BizException(String code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BizException(String msg) {
        super(msg);
        this.code = ResponseCodeEnum.ERROR.getCode();
        this.msg = msg;
    }

}
