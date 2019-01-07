package com.kang.codetool.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.kang.framework.KlConvert;
import com.mintq.conf.core.MintqConfClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Administrator
 */
@Configuration
@MapperScan(basePackages = { "com.kang.codetool.dao" })
public class DataConfig {

	private static Log logger = LogFactory.getLog(DataConfig.class);

	private String url = MintqConfClient.get("risk-control.postgresql.url");
	private String username = MintqConfClient.get("risk-control.postgresql.username");
	private String password= MintqConfClient.get("risk-control.postgresql.password");
	private String driverClass= MintqConfClient.get("risk-control.postgresql.driverClassName");
	private int maxActive= KlConvert.toInteger(MintqConfClient.get("risk-control.postgresql.maxActive"));
	private int minIdle= KlConvert.toInteger(MintqConfClient.get("risk-control.postgresql.minIdle"));
	private int initialSize= KlConvert.toInteger(MintqConfClient.get("risk-control.postgresql.initialSize"));
	private boolean testOnBorrow= KlConvert.toBoolean(MintqConfClient.get("risk-control.postgresql.testOnBorrow"));

	@Bean
	public DataSource druidDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		if (initialSize > 0) {
			dataSource.setInitialSize(initialSize);
		}
		if (minIdle > 0) {
			dataSource.setMinIdle(minIdle);
		}
		if (maxActive > 0) {
			dataSource.setMaxActive(maxActive);
		}
		dataSource.setTestOnBorrow(testOnBorrow);
		try {
			dataSource.init();
		} catch (SQLException e) {
			logger.error(e);
		}
		return dataSource;
	}

}
