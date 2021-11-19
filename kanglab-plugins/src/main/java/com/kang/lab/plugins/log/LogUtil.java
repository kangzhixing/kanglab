package com.kang.lab.plugins.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kang.lab.plugins.log.annotations.KlLog;
import com.kang.lab.utils.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志工具类
 *
 * @author kangzhixing
 * @date 2020-09-09 14:39
 */
@Slf4j
public class LogUtil {

    /**
     * 日志长度限制
     */
    public static int LOG_MAX_LENGTH = 3000;

    /**
     * 打印请求
     */
    public static LogInfo logRequest(JoinPoint joinPoint) {
        LogInfo logInfo = LogInfo.getInstance(joinPoint);
        if (logInfo.isLog()) {
            try {
                if (!logInfo.isLogRequestParam()) {
                    log.info("[REQUEST]{}", logInfo.getLogTitle());
                } else {
                    log.info("[REQUEST]{} param: {}", logInfo.getLogTitle(),
                            StringUtil.trySubstring(JSON.toJSONString(removeObjectCannotBeFormat(joinPoint.getArgs()), SerializerFeature.WriteMapNullValue), LOG_MAX_LENGTH));
                }

            } catch (Exception ex) {
                log.error("日志打印失败", ex);
            }
        }

        return logInfo;
    }

    /**
     * 打印请求
     */
    public static LogInfo logRequest(MethodInvocation methodInvocation) {
        LogInfo logInfo = LogInfo.getInstance(methodInvocation.getMethod());
        if (logInfo.isLog()) {
            try {
                if (!logInfo.isLogRequestParam()) {
                    log.info("[REQUEST]{}", logInfo.getLogTitle());
                } else {
                    log.info("[REQUEST]{} param: {}", logInfo.getLogTitle(),
                            StringUtil.trySubstring(JSON.toJSONString(removeObjectCannotBeFormat(methodInvocation.getArguments()), SerializerFeature.WriteMapNullValue), LOG_MAX_LENGTH));
                }

            } catch (Exception ex) {
                log.error("日志打印失败", ex);
            }
        }

        return logInfo;
    }

    /**
     * 剔除servlet对象
     *
     * @param args
     * @return
     */
    public static Object[] removeObjectCannotBeFormat(Object[] args) {
        if (args == null) {
            return null;
        }
        List<Object> list = new ArrayList<>();

        for (Object obj : args) {
            if (obj instanceof HttpServletRequest || obj instanceof HttpServletResponse) {
                continue;
            }
            list.add(obj);
        }

        return list.toArray();
    }

    /**
     * 打印返回
     *
     * @param logInfo 日志信息
     * @param result  请求结果
     */
    public static void logResponse(LogInfo logInfo, Object result) {
        if (!logInfo.isLog()) {
            return;
        }
        long costTime = System.currentTimeMillis() - logInfo.getStartTime();
        try {
            if (!logInfo.isLogResponseParam()) {
                log.info("[RESPONSE]{}[{}ms]", logInfo.getLogTitle(), costTime);
            } else {
                log.info("[RESPONSE]{}[{}ms] return: {}", logInfo.getLogTitle(), costTime,
                        StringUtil.trySubstring(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue), LOG_MAX_LENGTH));
            }
        } catch (Exception ex) {
            log.warn("日志打印失败", ex);
        }
    }

    /**
     * 通过ProceedingJoinPoint生成日志标题
     */
    public static String getLogTitle(JoinPoint joinPoint) {
        try {
            // 获取方法
            return getLogTitleByMethod(getMethodByJoinPoint(joinPoint));

        } catch (Exception ex) {
            return "";
        }
    }

    public static String getClassDescByMethod(Method method){
        // 优先输出swagger注释
        ApiOperation annotationApi = method.getAnnotation(ApiOperation.class);
        if (annotationApi != null && annotationApi.value().length() > 0) {
            return annotationApi.value();
        } else {
            KlLog annotationOpenLog = method.getAnnotation(KlLog.class);
            if (annotationOpenLog != null && annotationOpenLog.value().length() > 0) {
                return annotationOpenLog.value();
            }
        }
        return "";
    }

    /**
     * 通过方法获取日志标题
     * @param method
     * @return
     */
    public static String getLogTitleByMethod(Method method) {
        if (method == null) {
            return "";
        }
        String apiInformation = getClassDescByMethod(method);
        return String.format("%s[%s.%s]",
                apiInformation,
                method.getDeclaringClass().getSimpleName(),
                method.getName());
    }

    /**
     * 通过ProceedingJoinPoint获取方法
     */
    public static Method getMethodByJoinPoint(JoinPoint joinPoint) {
        try {
            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            return joinPoint.getTarget().getClass().getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}