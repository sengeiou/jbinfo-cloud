package cn.jbinfo.integration.redis;

import cn.jbinfo.integration.redis.prefix.redis.TokenRedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Token
 *
 * @author xiaobin
 * @create 2017-08-29 下午5:08
 **/
@Configuration
@EnableConfigurationProperties({TokenRedisProperties.class, RedisProperties.class})
public class TokenRedisConfiguration extends AbstractRedisConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private TokenRedisProperties tokenRedisProperties;

    @Bean("tokenRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        return buildRedisTemplate(redisProperties,tokenRedisProperties.getDb());
    }


}
