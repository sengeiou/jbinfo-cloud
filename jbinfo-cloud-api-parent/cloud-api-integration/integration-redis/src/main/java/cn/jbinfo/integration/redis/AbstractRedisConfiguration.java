package cn.jbinfo.integration.redis;

import cn.jbinfo.common.dal.cache.conf.CacheConfig;
import cn.jbinfo.common.utils.StringUtils;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis多库
 *
 * @author xiaobin
 * @create 2017-08-30 上午8:42
 **/
abstract class AbstractRedisConfiguration {

    private JedisConnectionFactory connectionFactory(String hostName, int port,
                                                     String password, int maxIdle, int maxTotal, int index,
                                                     long maxWaitMillis) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(hostName, port);
        if (!StringUtils.isEmpty(password)) {
            configuration.setPassword(RedisPassword.of(password));
        }

        if (index != 0) {
            configuration.setDatabase(index);
        }

        JedisConnectionFactory jedis = new JedisConnectionFactory(configuration);

        jedis.setPoolConfig(poolCofig(maxIdle, maxTotal, maxWaitMillis));
        // 初始化连接pool
        jedis.afterPropertiesSet();
        return jedis;
    }

    RedisTemplate<String, Object> buildRedisTemplate(RedisProperties redisProperties, int db) {
        RedisTemplate<String, Object> temple = new RedisTemplate<>();
        JedisConnectionFactory connectionFactory = connectionFactory(redisProperties.getHost(), redisProperties.getPort(),
                redisProperties.getPassword(),
                redisProperties.getJedis().getPool().getMaxIdle(), redisProperties.getJedis().getPool().getMaxActive(),
                db, redisProperties.getJedis().getPool().getMaxWait());
        temple.setConnectionFactory(connectionFactory);
        CacheConfig.initDomainRedisTemplate(temple, connectionFactory);
        return temple;
    }

    private JedisPoolConfig poolCofig(int maxIdle, int maxTotal,
                                      long maxWaitMillis) {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMaxTotal(maxTotal);
        poolCofig.setMaxWaitMillis(maxWaitMillis);
        return poolCofig;
    }
}
