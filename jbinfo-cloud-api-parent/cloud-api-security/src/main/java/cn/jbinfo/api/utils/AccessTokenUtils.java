package cn.jbinfo.api.utils;


import cn.jbinfo.api.constant.Resource;

/**
 * Created by xiaobin on 16/7/8.
 */
public class AccessTokenUtils {

    public static String buildAccessTokenCacheKey(String apiId) {
        return Resource.ACCESS_TOKEN_CACHE_KEY_PREFIX + apiId;
    }

}
