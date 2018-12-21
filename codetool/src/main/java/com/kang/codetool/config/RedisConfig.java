//package com.kang.codetool.config;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import redis.clients.jedis.JedisPoolConfig;
//
//
//@Configuration
//@EnableAutoConfiguration
//public class RedisConfig {
//
//    @Bean
//    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<String, byte[]>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        return redisTemplate;
//    }
//
//    @Bean
//    protected JedisPoolConfig getJedisPoolConfig(){
//        JedisPoolConfig  config =  new JedisPoolConfig();
//        return config;
//    }
//}
