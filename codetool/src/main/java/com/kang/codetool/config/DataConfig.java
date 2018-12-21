package com.kang.codetool.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Administrator
 */
@Configuration
@MapperScan(basePackages = { "com.kang.codetool.dao" })
public class DataConfig {

	@Bean
	@ConfigurationProperties(prefix = "postgresql")
	public DataSource druidDataSource() {
		DruidDataSource source = new DruidDataSource();
		return source;
	}

}
