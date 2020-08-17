package com.kang.codetool.aop;

import com.kang.framework.KlJson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.assertj.core.util.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

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
        log.info("[{}.{}] request: {}", proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                proceedingJoinPoint.getSignature().getName(),
                KlJson.toJSONString(proceedingJoinPoint.getArgs()));
        Object result = proceedingJoinPoint.proceed();
        log.info("[{}.{}] response: {}, cost: {}ms", proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                proceedingJoinPoint.getSignature().getName(),
                KlJson.toJSONString(result), System.currentTimeMillis() - start);
        return result;
    }

}