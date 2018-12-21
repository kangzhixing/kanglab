package com.kang.codetool.util;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Service
public class RedisLockUtil {

    Log logger = LogFactory.getLog(RedisLockUtil.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 执行多次的定时任务redis锁
     *
     * @param redisKey
     * @return
     */
    public boolean pollingTryLock(String redisKey, long timeout) {
        String key = redisKey + ":" + LocalDate.now();
        String value = key + " " + getLocalIP();
        return pollingTryLock(key, value, timeout);
    }

    /**
     * 执行多次的定时任务redis锁( 指定key value )
     *
     * @return
     */
    public boolean pollingTryLock(String key, String value, long timeout) {
        Boolean setIfAbsent = false;
        try {
            setIfAbsent = redisTemplate.opsForValue().setIfAbsent(key, value);
        } finally {
            if (setIfAbsent){
                redisTemplate.expire(key, timeout, TimeUnit.MINUTES);
            }
        }
        return setIfAbsent == null ? false : setIfAbsent;
    }

    public boolean tryLock(String redisKey) {
        String autoRejectKey = redisKey + ":" + LocalDate.now();
        String value = autoRejectKey + " " + getLocalIP();
        Boolean setIfAbsent;
        try {
            setIfAbsent = redisTemplate.opsForValue().setIfAbsent(autoRejectKey, value);
        } finally {
            redisTemplate.expire(autoRejectKey, 24, TimeUnit.HOURS);
        }
        return setIfAbsent == null ? false : setIfAbsent;
    }

    public String getLocalIP() {
        String localIP = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            // 获取本机IP
            localIP = addr.getHostAddress().toString();
        } catch (Exception e) {
            logger.info("获取本机ip失败");
        }
        return localIP;
    }
}
