package com.ticketplatform.ddd.infrastructure.cache.redis.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketplatform.ddd.infrastructure.cache.redis.RedisInfrasService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisInfrasServiceImpl implements RedisInfrasService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setString(String key, String value) {
        if (StringUtils.hasLength(key)) { // check null or ""
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getString(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
        if (!StringUtils.hasLength(key)) {
            log.info("Set redis::null, {}", StringUtils.hasLength(key));
            return;
        }
        try {
            // Convert object to JSON string before storing
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonValue = objectMapper.writeValueAsString(value);
//            redisTemplate.opsForValue().set(key, jsonValue);
            redisTemplate.opsForValue().set(key, value);
            log.info("Set redis successful: key={}", key);
        } catch (Exception e) {
            log.error("setObject error: {}", e.getMessage());
        }
    }

    @Override
    public void setObjectTTL(String key, Object value) {
        if(!StringUtils.hasLength(key)){
            log.info("Set redis TTL::null, {}", StringUtils.hasLength(key));
            return;
        }
        try {
            redisTemplate.opsForValue().set(key, value, 10, TimeUnit.SECONDS);
            log.info("Set redis TTL successful: key={}, TTL=10 seconds", key);
        } catch (Exception e) {
            log.error("setObjectTTL error: {}", e.getMessage());
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        Object result = redisTemplate.opsForValue().get(key);
        log.info("get cache: {}", result);
        if (result == null) {
            return null;
        }

        // if result is a LinkedHashMap
        if (result instanceof Map) {
            try {
                // transfer LinkedHashMap to target object
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.convertValue(result, targetClass);
            } catch (IllegalArgumentException e) {
                log.error("Error converting LinkedHashMap to object: {}", e.getMessage());
                return null;
            }
        }

        // if result is String, implement normal transfer
        if (result instanceof String) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue((String) result, targetClass);
            } catch (JsonProcessingException e) {
                log.error("Error deserializing JSON to object: {}", e.getMessage());
                return null;
            }
        }
        return null; // or return an optional exception
    }
}
