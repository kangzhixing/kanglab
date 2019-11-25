package com.kang.codetool.config.dataSource;

import com.mysql.cj.util.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        //获取当前线程的数据源，如果不存在使用master数据源
        String datasource = DBContextHolder.getDataSource();
        if (StringUtils.isNullOrEmpty(datasource)) {
            datasource = "master";
        }
        logger.info("当前数据源: " + datasource);
        return datasource;

    }
}