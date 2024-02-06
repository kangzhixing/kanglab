package com.kang.lab.plugins.log;

import com.kang.lab.plugins.log.annotation.*;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class LogInfo {

    /**
     * log标题
     */
    private String logTitle;

    /**
     * 是否打印日志
     */
    private boolean isLog;

    /**
     * 是否不打印入参
     */
    private boolean skipPrintParams;

    /**
     * 是否不打印出参
     */
    private boolean skipPrintResult;

    /**
     * 开始时间戳
     */
    private long startTime;

    /**
     * 私有构造
     *
     * @param method
     */
    private LogInfo(Method method) {
        this.startTime = System.currentTimeMillis();
        this.logTitle = LogUtil.getLogTitleByMethod(method);

        this.isLog = method.getDeclaredAnnotation(NoLog.class) == null;

        OpenLog annotationOpenLog = method.getDeclaredAnnotation(OpenLog.class);
        this.skipPrintParams = method.getDeclaredAnnotation(NoLogRequest.class) != null
                || (annotationOpenLog != null && !annotationOpenLog.printParams());
        this.skipPrintResult = "void".equals(method.getReturnType().getName())
                || method.getDeclaredAnnotation(NoLogResponse.class) != null
                || (annotationOpenLog != null && !annotationOpenLog.printResult());
    }

    /**
     * 获取实例
     *
     * @param method
     * @return
     */
    public static LogInfo getInstance(Method method) {
        return new LogInfo(method);
    }
}
