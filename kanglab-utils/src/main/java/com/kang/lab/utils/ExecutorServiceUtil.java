package com.kang.lab.utils;

import org.slf4j.MDC;

import java.util.concurrent.ExecutorService;

/**
 * 委托在多线程内添加traceId
 *
 * @author kangzhixing
 */
public class ExecutorServiceUtil {

    public static String TRACE_ID_KEY = "traceId";

    private ExecutorService executorService;

    private ExecutorServiceUtil(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static ExecutorServiceUtil getInstance(ExecutorService executorService) {
        return new ExecutorServiceUtil(executorService);
    }

    public void execute(DelegateTask delegateTask) {
        final String traceId = MDC.get(TRACE_ID_KEY);
        executorService.execute(() -> {
            MDC.put(TRACE_ID_KEY, traceId);
            delegateTask.execute();
        });
    }
}
