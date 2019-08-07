package com.management.demo.repository.redis;

import java.util.Optional;

public interface BaseRedis<T> {

    void addInRedis(String key,T value);

    Optional<T> fetchFromRedis(String key);

    void deleteFromRedis(String key);

}
