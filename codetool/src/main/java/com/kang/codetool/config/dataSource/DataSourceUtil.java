package com.kang.codetool.config.dataSource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DataSourceUtil {

    public static Map<Object, Object> dataSourceMap = new HashMap<>();

    public static void initDataSource() {
        //获取masterDataSource
        DruidDataSource masterDataSource = (DruidDataSource) SpringContextUtil.getBean("masterDataSource");
        addDataSource("master", masterDataSource);
    }

    public static void flushDataSource() {
        //获取spring管理的dynamicDataSource
        DynamicDataSource myDynamicDataSource = (DynamicDataSource) SpringContextUtil.getBean("dynamicDataSource");
        //将数据源设置到 targetDataSources
        myDynamicDataSource.setTargetDataSources(dataSourceMap);
        //将 targetDataSources 中的连接信息放入 resolvedDataSources 管理
        myDynamicDataSource.afterPropertiesSet();
    }

    public static void setDefaultTargetDataSource(String key) {
        dataSourceMap = Maps.newHashMap(key, dataSourceMap.get(key));
        flushDataSource();
    }

    public static void addDataSource(String key, DruidDataSource masterDataSource) {
        dataSourceMap.put(key, masterDataSource);
        flushDataSource();
    }

    public static void addDataSource(String key, String url) {
        //在此处可以查询出所有的数据源（例如，配置文件，数据库）然后添加
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl(url);
        //添加数据源到map
        addDataSource(key, druidDataSource);
    }
}
