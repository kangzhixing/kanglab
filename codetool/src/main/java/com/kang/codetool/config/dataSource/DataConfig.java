package com.kang.codetool.config.dataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.kang.framework.KlConvert;
import com.mintq.conf.core.MintqConfClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
@Configuration
@MapperScan(basePackages = {"com.kang.codetool.dao"})
public class DataConfig {

    @Primary
    @Bean(value = "masterDataSource")
    public DataSource masterDataSource() {
        DruidDataSource source = new DruidDataSource();
        source.setPassword(MintqConfClient.get("risk-platform-manage.spring.datasource.password"));
        source.setDriverClassName(MintqConfClient.get("risk-platform-manage.spring.datasource.driverClassName"));
        source.setUrl(MintqConfClient.get("risk-platform-manage.spring.datasource.url"));
        source.setDbType(MintqConfClient.get("risk-platform-manage.spring.datasource.dbType"));
        source.setUsername(MintqConfClient.get("risk-platform-manage.spring.datasource.username"));
        source.setInitialSize(KlConvert.tryToInteger(MintqConfClient.get("risk-platform-manage.spring.datasource.initialSize")));
        source.setMaxActive(KlConvert.tryToInteger(MintqConfClient.get("risk-platform-manage.spring.datasource.maxActive")));
        source.setMaxWait(KlConvert.tryToLong(MintqConfClient.get("risk-platform-manage.spring.datasource.maxWait")));
        source.setMaxIdle(KlConvert.tryToInteger(MintqConfClient.get("risk-platform-manage.spring.datasource.minIdle")));
        source.setTimeBetweenEvictionRunsMillis(KlConvert.tryToInteger(MintqConfClient.get("risk-platform-manage.spring.datasource.timeBetweenEvictionRunsMillis")));
        source.setValidationQuery(MintqConfClient.get("risk-platform-manage.spring.datasource.validationQuery"));
        source.setTestWhileIdle(KlConvert.tryToBoolean(MintqConfClient.get("risk-platform-manage.spring.datasource.testWhileIdle")));
        source.setTestOnBorrow(KlConvert.tryToBoolean(MintqConfClient.get("risk-platform-manage.spring.datasource.testOnBorrow")));
        source.setTestOnReturn(KlConvert.tryToBoolean(MintqConfClient.get("risk-platform-manage.spring.datasource.testOnReturn")));
        source.setPoolPreparedStatements(KlConvert.tryToBoolean(MintqConfClient.get("risk-platform-manage.spring.datasource.poolPreparedStatements")));
        source.setMaxOpenPreparedStatements(KlConvert.tryToInteger(MintqConfClient.get("risk-platform-manage.spring.datasource.maxOpenPreparedStatements")));
        return source;
    }

    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDatasource = new DynamicDataSource();
        //设置默认数据源
        dynamicDatasource.setDefaultTargetDataSource(masterDataSource());
        //配置多数据源
        Map<Object, Object> dsMap = new HashMap<>();
        //将多数据源放到数据源池中
        dynamicDatasource.setTargetDataSources(dsMap);
        return dynamicDatasource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());//将动态数据源bean配置到sqlsessionfactory
        return sqlSessionFactoryBean.getObject();
    }
    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}
