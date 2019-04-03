package cn.jbinfo.integration.redis.prefix.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 公共Token缓存
 *
 * @author xiaobin
 * @create 2017-08-29 下午4:52
 **/
@Component("tokenRedisProperties")
@ConfigurationProperties(prefix = "phenixtoken")
@Getter
@Setter
public class TokenRedisProperties{

    private static final long serialVersionUID = 7027785958875323963L;

    private Integer db;
}
