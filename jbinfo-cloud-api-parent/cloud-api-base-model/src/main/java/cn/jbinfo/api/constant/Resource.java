package cn.jbinfo.api.constant;

/**
 * 系统运行参数
 * Created by xiaobin on 16/7/8.
 */
public interface Resource {

    String REQ_ACCESS_TOKEN = "token";

    /**
     * token过期时间-小时(按照毫秒)
     */
    int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000;

    /**
     * token key 前缀
     */
    String ACCESS_TOKEN_CACHE_KEY_PREFIX = "access_token_";

    //redis数据库索引
    Integer REDIS_DEFAULT_DB = 1;

    //認證相關
    Integer REDIS_AUTH_DB = 2;

    //媒资相关
    Integer REDIS_MEDIA_DB = 3;

    //支付相关
    Integer REDIS_PAY_DB = 4;

}
