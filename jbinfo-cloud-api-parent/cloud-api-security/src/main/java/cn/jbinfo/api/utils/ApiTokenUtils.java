package cn.jbinfo.api.utils;

import cn.jbinfo.api.acl.AccessToken;
import cn.jbinfo.api.constant.ApiCode;
import cn.jbinfo.api.context.RestContextManager;
import cn.jbinfo.api.context.cache.TokenCache;
import cn.jbinfo.api.exception.ApiException;
import cn.jbinfo.cloud.core.utils.SpringContextHolder;
import cn.jbinfo.rpc.api.model.DeviceInfo;
import cn.jbinfo.rpc.api.model.TenantUserInfo;

import java.util.Map;

/**
 * Created by xiaobin on 2016/10/27.
 */
public class ApiTokenUtils {

    public static final String TOKEN_MEMBER = "member";
    public static final String TOKEN_DEVICE_MEMBER = "device_member";
    public static final String TOKEN_APP_ID = "appId";
    public static final String TOKEN_DEVISE = "devise";
    public static final String TOKEN_DEVISE_ID = "deviseId";
    public static final String TOKEN_APP_NAME = "appName";

    public static AccessToken getToken() {
        return RestContextManager.getContext().getToken();
    }

    public static Map<String, Object> getTokenExtendsMap() {
        AccessToken token = RestContextManager.getContext().getToken();
        if (token == null)
            return null;

        return token.getAdditionalInformation();
    }

    /**
     * app端用户
     *
     * @return
     */
    public static TenantUserInfo getMember() {
        AccessToken token = RestContextManager.getContext().getToken();
        if (token == null) {
            return null;
        }
        if (token.getAdditionalInformation().get(TOKEN_APP_ID) == null) {
            throw new ApiException(ApiCode.AUTH_LESS, "没有找到服务号标识");
        }
        if (token.getAdditionalInformation().get(TOKEN_MEMBER) == null) {
            throw new ApiException(ApiCode.AUTH_LESS, "用户没有登录");
        }
        return (TenantUserInfo) token.getAdditionalInformation().get(TOKEN_MEMBER);
    }

    public static Object getTokenObj() {
        AccessToken access = RestContextManager.getContext().getToken();
        if (access == null) {
            return null;
        }
        if (access.getAdditionalInformation().containsKey(ApiTokenUtils.TOKEN_DEVICE_MEMBER)) {//终端电视
            return ApiTokenUtils.getDeviceUser();
        } else if (access.getAdditionalInformation().containsKey(ApiTokenUtils.TOKEN_MEMBER)) {//手机
            return ApiTokenUtils.getMember();
        }
        return null;
    }

    /**
     * 终端用户
     *
     * @return
     */
    public static DeviceInfo getDeviceUser() {
        AccessToken token = RestContextManager.getContext().getToken();
        if (token == null) {
            return null;
        }
        if (token.getAdditionalInformation().get(TOKEN_APP_ID) == null) {
            throw new ApiException(ApiCode.AUTH_LESS, "没有找到服务号标识");
        }
        if (token.getAdditionalInformation().get(TOKEN_DEVICE_MEMBER) == null) {
            throw new ApiException(ApiCode.AUTH_LESS, "设备没有登录");
        }
        return (DeviceInfo) token.getAdditionalInformation().get(TOKEN_DEVICE_MEMBER);
    }

    public static DeviceInfo getDeviceUser(String tokenKey) {

        TokenCache tokenCache = SpringContextHolder.getBean("tokenCache");
        AccessToken token = tokenCache.getToken(tokenKey);
        if (token == null) {
            return null;
        }
        if (token.getAdditionalInformation().get(TOKEN_APP_ID) == null) {
            //throw new ApiException(ApiCode.AUTH_LESS, "没有找到服务号标识");
            return null;
        }
        if (token.getAdditionalInformation().get(TOKEN_DEVICE_MEMBER) == null) {
            //throw new ApiException(ApiCode.AUTH_LESS, "设备没有登录");
            return null;
        }
        return (DeviceInfo) token.getAdditionalInformation().get(TOKEN_DEVICE_MEMBER);
    }

}
