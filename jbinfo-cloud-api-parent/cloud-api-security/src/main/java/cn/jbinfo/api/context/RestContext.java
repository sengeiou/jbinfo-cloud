package cn.jbinfo.api.context;

import cn.jbinfo.api.acl.AccessToken;
import cn.jbinfo.api.utils.ApiTokenUtils;

import java.util.HashMap;

/**
 * API 参数的上下文存储
 * Created by xiaobin on 16/7/8.
 */
public class RestContext extends HashMap<String, Object> {

    /**
     *
     */
    private static final long serialVersionUID = 6732312763880543546L;

    public static final String BUCKET = "bucket";

    public static final String TOKEN = ApiTokenUtils.TOKEN_DEVISE;

    public void setBucket(String bucket) {
        super.put(BUCKET, bucket);
    }

    public String getBucket() {
        return String.valueOf(super.get(BUCKET));
    }

    public boolean containsBucket() {
        return super.containsKey(BUCKET);
    }

    public void setToken(AccessToken accessToken) {
        super.put(TOKEN, accessToken);
    }

    public AccessToken getToken() {
        return (AccessToken) super.get(TOKEN);
    }

    public boolean containsAccessToken() {
        return super.containsKey(TOKEN);
    }

}