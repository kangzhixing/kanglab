package com.kang.codetool.config;

import com.kang.framework.KlConvert;
import com.mintq.conf.core.MintqConfClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by mal on 3/16/17.
 */
@Configuration
@EnableAutoConfiguration
public class RedisConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<String, byte[]>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    protected JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig  config =  new JedisPoolConfig();
        config.setMaxIdle(KlConvert.tryToInteger(MintqConfClient.get("risk-china.spring.redis.maxIdle")));
        config.setMaxTotal(KlConvert.tryToInteger(MintqConfClient.get("risk-china.spring.redis.maxActive")));
        config.setMaxWaitMillis(KlConvert.tryToInteger(MintqConfClient.get("risk-china.spring.redis.maxWait")));

        return config;
    }

    @Bean
    public RedisConnectionFactory connectionFactory(JedisPoolConfig poolConfig) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
        connectionFactory.setHostName(MintqConfClient.get("risk-china.spring.redis.host"));
        connectionFactory.setPassword(MintqConfClient.get("risk-china.spring.redis.password"));
        connectionFactory.setPort(KlConvert.tryToInteger(MintqConfClient.get("risk-china.spring.redis.port")));
        return connectionFactory;
    }

}
