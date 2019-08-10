package com.management.demo.repository.redis;


import com.fasterxml.jackson.core.type.TypeReference;
import com.management.demo.entity.WorkEntity;
import com.management.demo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Optional;


@Repository
@Slf4j
public class WorkRedis implements BaseRedis{

    private JedisPool jedisPool;

    @Override
    public void addInRedis(String key, Object value) {
        try(Jedis jedis = jedisPool.getResource()){
            String jsonValue = JsonUtil.serialize(value);
            jedis.set(key,jsonValue);
            jedis.expire(key,120000);
            log.info("Add new object in redis, with {} key",key);
        }catch (Exception e){
            log.warn("Redis error : ");
        }
    }

    @Override
    public Optional<WorkEntity> fetchFromRedis(String key) {
        try(Jedis jedis = jedisPool.getResource()){
            String jsonValue = jedis.get(key);
            if (!jsonValue.trim().isEmpty()){
                return Optional.of(JsonUtil.deserialize(jsonValue, new TypeReference<WorkEntity>() {
                }));
            }
        } catch (IOException e) {
            log.warn("Redis Error : ");
        }
        log.info("Object with {} key not found or expired",key);
        return Optional.empty();
    }

    @Override
    public void deleteFromRedis(String key) {
        try(Jedis jedis = jedisPool.getResource()){
            jedis.del(key);
            log.info("Object with {} removed from redis",key);
        }catch (Exception e){
            log.warn("Redis error : ");
        }
    }
}
