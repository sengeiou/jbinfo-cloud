package cn.jbinfo.api.context;

import cn.jbinfo.api.acl.AccessToken;
import cn.jbinfo.api.constant.ApiCode;
import cn.jbinfo.api.context.cache.TokenCache;
import cn.jbinfo.api.exception.ApiException;
import cn.jbinfo.cloud.core.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaobin on 16/7/8.
 */
public class RestContextManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestContextManager.class);

    private static final ThreadLocal<RestContext> REST_CONTEXT = new ThreadLocal<>();

    public static void setContext(RestContext context) {
        REST_CONTEXT.set(context);
    }

    public static RestContext getContext() {
        if (REST_CONTEXT.get() == null) {
            return new RestContext();
        }
        return REST_CONTEXT.get();
    }

    public static void initAccessToken(String token) {
        RestContext context = REST_CONTEXT.get();
        if (null == context) {
            context = new RestContext();
        }
        if (StringUtils.isNotBlank(token)) {
            TokenCache cacheManager = SpringContextHolder.getBean(TokenCache.class);
            if (null != cacheManager) {
                AccessToken accessToken = cacheManager.getToken(token);
                if (accessToken != null) {
                    context.setToken(accessToken);
                } else {
                    LOGGER.info("傳入token:" + token);
                    throw new ApiException(ApiCode.AUTH_ERROR, "请重新登录");
                }
            }
        }
        REST_CONTEXT.set(context);
    }

    public static void clearContext() {
        REST_CONTEXT.remove();
    }


}