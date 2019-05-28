package com.estatesoft.ris.wx.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    protected RedisTemplate redisTemplate;


    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @param key
     * @param value
     * @param expire 过期分钟
     */
    public void put(String key, String value, long expire) {
        if (expire > 0) {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.MINUTES);//1分钟过期
        }
    }

    /**
     * 用系统默认的超时时间
     *
     * @param key
     * @param value
     */
    public void putDefaultExpire(String key, String value) {
        redisTemplate.opsForValue().set(key, value, 30, TimeUnit.MINUTES);//1分钟过期
    }

    public String get(String key) {
        ValueOperations<String, String> ops = this.redisTemplate.opsForValue();
        return String.valueOf(ops.get(key));
    }
}