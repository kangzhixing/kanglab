package com.kang.lab.plugins.log;

import com.kang.lab.plugins.log.annotation.OpenLog;
import com.kang.lab.plugins.utils.json.JsonUtil;
import com.kang.lab.utils.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

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
    public static LogInfo logRequest(Method method, Object[] args) {
        LogInfo logInfo = LogInfo.getInstance(method);
        if (logInfo.isLog()) {
            try {
                if (logInfo.isSkipPrintParams()) {
                    log.info("[IN]{}", logInfo.getLogTitle());
                } else {
                    log.info("[IN]{} param: {}", logInfo.getLogTitle(), StringUtil.trySubstring(JsonUtil.desensitize(removeObjectCannotBeFormat(args)), LOG_MAX_LENGTH));
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
    public static Object[] removeObjectCannotBeFormat(Object... args) {
        if (args == null) {
            return null;
        }
        // 先遍历一遍，看是否存在不能序列化的对象，避免每次都创建新数组
        int countBadObject = 0;
        for (Object obj : args) {
            if (checkObjectCannotBeFormat(obj)) {
                countBadObject++;
            }
        }
        if (countBadObject == 0) {
            return args;
        }
        if (countBadObject == args.length) {
            return new Object[0];
        }
        Object[] newArr = new Object[args.length - countBadObject];
        int newArrIndex = 0;
        for (Object obj : args) {
            if (checkObjectCannotBeFormat(obj)) {
                continue;
            }
            newArr[newArrIndex++] = obj;
        }
        return newArr;
    }

    /**
     * 是否是不能格式化的对象
     */
    private static boolean checkObjectCannotBeFormat(Object obj) {
        return obj instanceof HttpServletRequest || obj instanceof HttpServletResponse || obj instanceof Model;
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
            if (logInfo.isSkipPrintResult() || checkObjectCannotBeFormat(result)) {
                log.info("[OUT]{}[{}ms]", logInfo.getLogTitle(), costTime);
            } else {
                log.info("[OUT]{}[{}ms] return: {}", logInfo.getLogTitle(), costTime,
                        StringUtil.trySubstring(JsonUtil.desensitize(result), LOG_MAX_LENGTH));
            }
        } catch (Exception ex) {
            log.error("日志打印失败", ex);
        }
    }

    public static String getMethodDescByMethod(Method method) {
        // 优先输出OpenLog的方法描述
        OpenLog openLogAnno = method.getAnnotation(OpenLog.class);
        if (openLogAnno != null && !openLogAnno.value().isEmpty()) {
            return openLogAnno.value();
        } else {
            ApiOperation apiOperationAnno = method.getAnnotation(ApiOperation.class);
            if (apiOperationAnno != null && !apiOperationAnno.value().isEmpty()) {
                return apiOperationAnno.value();
            }
        }
        return "";
    }

    public static String getLogTitleByMethod(Method method) {
        if (method == null) {
            return "";
        }
        String apiInformation = getMethodDescByMethod(method);
        return String.format("%s[%s.%s]", apiInformation, method.getDeclaringClass().getSimpleName(), method.getName());
    }

    /**
     * 通过JoinPoint获取方法
     */
    public static Method getMethodByJoinPoint(JoinPoint joinPoint) {
        try {
            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            return joinPoint.getTarget().getClass().getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.error("通过JoinPoint获取方法失败");
            return null;
        }
    }
}