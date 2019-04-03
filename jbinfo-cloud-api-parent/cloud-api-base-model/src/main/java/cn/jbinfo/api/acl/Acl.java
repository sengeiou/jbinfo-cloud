package cn.jbinfo.api.acl;

/**
 * Created by xiaobin on 16/7/8.
 */
public interface Acl {

    /**
     * 接受的Token
     *
     * @return
     */
    String getAccessToken();

    /**
     * 密匙
     * @return
     */
    String getSecretKey();

    /**
     * 应用标识
     * @return
     */
    String getBucket();

    /**
     * 系统名称
     *
     * @return
     */
    String getBucketName();

    /**
     * 允许的IP,不配置则不做限制
     *
     * @return
     */
    String getAllowIp();

    /**
     * 1 可用 0 禁用
     * @return
     */
    String getStatus();
}
