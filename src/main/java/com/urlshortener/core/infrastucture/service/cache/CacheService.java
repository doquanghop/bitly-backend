package com.urlshortener.core.infrastucture.service.cache;

import java.util.Set;

public interface CacheService {
    void put(String key, Object value);


    void put(String key, Object value, long ttl);

    <T> T get(String key, Class<T> clazz);

    void delete(String key);

    boolean exists(String key);

    Set<String> keys(String pattern);

    void addToBlacklist(String key, long ttl);

    boolean isBlacklisted(String key);

    void removeFromBlacklist(String key, long ttl);
}
