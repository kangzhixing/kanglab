package com.kang.codetool.aop;

import com.kang.codetool.common.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public RestResponse customException(Exception e) {
        log.error("系统异常", e);
        return RestResponse.fail(e.getMessage());
    }
}