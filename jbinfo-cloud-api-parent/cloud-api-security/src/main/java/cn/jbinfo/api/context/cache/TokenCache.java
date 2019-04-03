package cn.jbinfo.api.context.cache;

import cn.jbinfo.api.acl.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * token认证缓存
 *
 * @author xiaobin
 * @create 2017-08-28 上午9:02
 **/
@Component
public class TokenCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);

    @Qualifier("tokenRedisTemplate")
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取token
     *
     * @param token
     * @return
     */
    public AccessToken getToken(String token) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        return (AccessToken) operation.get(token);
    }

    public void putObject(String key, Object value, long t, TimeUnit timeUnit) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        operation.set(key, value, t, timeUnit);
    }

    /**
     * 获取apiValue
     *
     * @param apiKey
     * @param deviceUserId
     * @return
     */
    public Object getObject(String apiKey, String deviceUserId) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        return operation.get(apiKey + "#" + deviceUserId);
    }

    public <T> T removeObject(String key) {
        redisTemplate.delete(key);
        return null;
    }

    public void putObject(String key, String hashKey, Object value) {
        LOGGER.info("存储token,key=" + key);
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        operation.set(key + "#" + hashKey, value, 12L, TimeUnit.HOURS);
    }


}
