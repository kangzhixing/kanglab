package com.kang.codetool.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogControllerAspect {

    @Pointcut("execution(public * com.kang.codetool.controller..*.*(..))")
    private void logController() {
    }

    @Around("logController()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("[REQUEST][{}.{}] param: {}", proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                proceedingJoinPoint.getSignature().getName(),
                proceedingJoinPoint.getArgs() == null ? "" : JSON.toJSONString(proceedingJoinPoint.getArgs()));
        Object result = proceedingJoinPoint.proceed();
        log.info("[RESPONSE][{}.{}][{}ms] result: {}", proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                proceedingJoinPoint.getSignature().getName(), System.currentTimeMillis() - start,
                result == null ? "" : JSON.toJSONString(result));
        return result;
    }

}