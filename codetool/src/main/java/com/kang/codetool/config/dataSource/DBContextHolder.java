package com.kang.codetool.config.dataSource;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

public class DBContextHolder {
    // 对当前线程的操作-线程安全的
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
 
    // 调用此方法，切换数据源
    public static void setDataSource(String dataSource) {
        if (DataSourceUtil.dataSourceMap.containsKey(dataSource)) {
            contextHolder.set(dataSource);
        } else {
            throw new RuntimeException("数据源:" + dataSource + "不存在");
        }
    }
 
    // 获取数据源
    public static String getDataSource() {
        return contextHolder.get();
    }
 
    // 删除数据源
    public static void clearDataSource() {
        contextHolder.remove();
    }

}