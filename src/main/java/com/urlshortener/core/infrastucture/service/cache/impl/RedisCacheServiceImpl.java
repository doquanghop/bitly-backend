package com.urlshortener.core.infrastucture.service.cache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.core.infrastucture.service.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheServiceImpl implements CacheService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String BLACKLIST_KEY_PREFIX = "blacklist:";


    @Autowired
    public RedisCacheServiceImpl(StringRedisTemplate  redisTemplate,ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void put(String key, Object value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object for Redis", e);
        }
    }

    @Override
    public void put(String key, Object value, long ttl) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
//            throw new BusinessException("Failed to serialize object for Redis: " + e);
        }
    }

    @Override
    public <T> T get(String key,Class<T> clazz) {
        String jsonValue = (String) redisTemplate.opsForValue().get(key);
        if (jsonValue == null) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonValue, clazz);
        } catch (IOException e) {
//            throw new BusinessException("Failed to deserialize object from Redis: " + e);
        }
        return null;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public void addToBlacklist(String key, long ttl) {
        put(BLACKLIST_KEY_PREFIX + key, "blacklisted", ttl);
    }

    @Override
    public boolean isBlacklisted(String key) {
        return exists(BLACKLIST_KEY_PREFIX + key);
    }

    @Override
    public void removeFromBlacklist(String key, long ttl) {
        delete(BLACKLIST_KEY_PREFIX + key);
    }
}
