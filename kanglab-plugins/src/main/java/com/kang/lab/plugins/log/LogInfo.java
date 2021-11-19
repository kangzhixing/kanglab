package com.kang.lab.plugins.log;

import com.kang.lab.plugins.log.annotations.NoLog;
import com.kang.lab.plugins.log.annotations.NoLogRequest;
import com.kang.lab.plugins.log.annotations.NoLogResponse;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

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
     * 是否打印入参
     */
    private boolean isLogRequestParam;

    /**
     * 是否打印出参
     */
    private boolean isLogResponseParam;

    /**
     * 开始时间戳
     */
    private long startTime;

    /**
     * 私有构造
     * @param method
     */
    private LogInfo(Method method) {
        this.startTime = System.currentTimeMillis();
        this.logTitle = LogUtil.getLogTitleByMethod(method);

        NoLog annotationNoLog = method.getDeclaredAnnotation(NoLog.class);
        this.isLog = annotationNoLog == null;

        NoLogRequest annotationNoReq = method.getDeclaredAnnotation(NoLogRequest.class);
        this.isLogRequestParam = annotationNoReq == null;

        NoLogResponse annotationNoResp = method.getDeclaredAnnotation(NoLogResponse.class);
        this.isLogResponseParam = annotationNoResp == null && !"void".equals(method.getReturnType().getName());
    }

    /**
     * 获取实例
     * @param joinPoint
     * @return LogInfo
     */
    public static LogInfo getInstance(JoinPoint joinPoint) {
        return new LogInfo(LogUtil.getMethodByJoinPoint(joinPoint));
    }

    /**
     * 获取实例
     * @param method
     * @return
     */
    public static LogInfo getInstance(Method method) {
        return new LogInfo(method);
    }
}
