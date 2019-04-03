package cn.jbinfo.integration.alioss.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 描述: oss 配置
 */
@ConfigurationProperties(prefix = "oss")
@Getter
@Setter
public class OSSProperties {

    private String endpoint;
    private String website;
    private String accessKeyId;
    private String accessKeySecret;
    /**
     * 鉤子
     */
    private String bucketName;
    private Client client;
}
