package cn.jbinfo.common.dal.cache.impl;

import cn.jbinfo.common.dal.cache.Cache;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by xiaobin on 16/7/14.
 */
public class RedisCache implements Cache {


    public RedisTemplate<String, Object> redisTemplate;

    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setDatabase(int db) {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void putObject(String key, Object value) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        operation.set(key, value);
    }

    @Override
    public void putObject(String key, String hashKey, Object value) {
        HashOperations<String, String, Object> operation = redisTemplate.opsForHash();
        operation.put(key, hashKey, value);
    }

    @Override
    public void putObject(String key, Object value, long seconds) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        operation.set(key, value, seconds, TimeUnit.MILLISECONDS);
    }


    @Override
    public void putObject(String key, Object value, long t, TimeUnit timeUnit) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        operation.set(key, value, t, timeUnit);
    }

    @Override
    public <T> T getObject(String key) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        return (T) operation.get(key);
    }

    @Override
    public <T> T getObject(String key, String hashKey) {
        HashOperations<String, String, Object> operation = redisTemplate.opsForHash();
        return (T) operation.get(key, hashKey);
    }

    @Override
    public Map<String, Object> getHasObject(String key) {
        HashOperations<String, String, Object> operation = redisTemplate.opsForHash();
        return operation.entries(key);
    }

    @Override
    public <T> T removeObject(String key) {
        redisTemplate.delete(key);
        return null;
    }

    @Override
    public <T> T removeObject(String key, String hashKey) {
        HashOperations<String, String, Object> operation = redisTemplate.opsForHash();
        operation.delete(key, hashKey);
        return null;
    }

    @Override
    public void clear(String id) {

    }
}
