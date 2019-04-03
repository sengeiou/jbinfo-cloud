package cn.jbinfo.common.dal.cache.conf;

import cn.jbinfo.common.dal.cache.Cache;
import cn.jbinfo.common.dal.cache.CacheManager;
import cn.jbinfo.common.dal.cache.impl.RedisCache;
import cn.jbinfo.common.dal.cache.support.SimpleCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by xiaobin on 2017/8/13.
 */
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager getCacheManager(Cache cache) {
        CacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCache(cache);
        return cacheManager;
    }

    @Bean
    public Cache getCache(RedisTemplate<String, Object> redisTemplate) {
        return new RedisCache(redisTemplate);
    }

    @Autowired
    JedisConnectionFactory jedisConnectionFactory;

    /**
     * 实例化 RedisTemplate 对象
     *
     * @return
     */
    @Bean("redisTemplate")
    @Scope("prototype")
    public RedisTemplate<String, Object> getRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, jedisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 设置数据存入 redis 的序列化方式
     *
     * @param redisTemplate
     * @param factory
     */
    public static void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, JedisConnectionFactory factory) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
    }


   /* @Bean
    public Cache getCache() {
        return new ConcurrentMapCache();
    }*/

}
