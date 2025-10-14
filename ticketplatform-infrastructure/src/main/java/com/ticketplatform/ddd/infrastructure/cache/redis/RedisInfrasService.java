package com.ticketplatform.ddd.infrastructure.cache.redis;

public interface RedisInfrasService {
    void setString(String key, String value);
    String getString(String key);

    void setObject(String key, Object value);
    void setObjectTTL(String key, Object value); // set TTL cache to be expired after 10 second
    <T> T getObject(String key, Class <T> targetClass);

    void deleteKey(String key);

}
