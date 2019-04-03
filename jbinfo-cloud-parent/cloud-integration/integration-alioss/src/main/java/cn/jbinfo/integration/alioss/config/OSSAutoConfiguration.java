package cn.jbinfo.integration.alioss.config;

import cn.jbinfo.integration.alioss.OSSServiceFactory;
import cn.jbinfo.integration.alioss.impl.ALiOSSService;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.comm.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PreDestroy;

/**
 * 描述: aliyun oss 自动配置
 */
@ConditionalOnClass(OSSClient.class)
@EnableConfigurationProperties(OSSProperties.class)
public class OSSAutoConfiguration {

    @Autowired
    OSSProperties ossProperties;


    private OSSClient ossClient;

    @PreDestroy
    public void close() {
        try {
            ossServiceFactory().shutdown();
        } catch (Exception ex) {
        }
        if (this.ossClient != null) {
            this.ossClient.shutdown();
        }
    }

    @Bean(name = "clientConfiguration")
    @ConditionalOnMissingBean
    public ClientConfiguration clientConfiguration(OSSProperties ossProperties) {
        Client client = ossProperties.getClient();
        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setMaxConnections(client.getMaxConnections());
        configuration.setSocketTimeout(client.getSocketTimeout());
        configuration.setConnectionTimeout(client.getConnectionTimeout());
        configuration.setConnectionRequestTimeout(client.getConnectionRequestTimeout());
        client.setIdleConnectionTime(client.getIdleConnectionTime());
        configuration.setMaxErrorRetry(client.getMaxErrorRetry());
        configuration.setSupportCname(client.isSupportCname());
        configuration.setSLDEnabled(client.isSldEnabled());
        if (Protocol.HTTP.toString().equals(client.getProtocol())) {
            configuration.setProtocol(Protocol.HTTP);
        } else if (Protocol.HTTPS.toString().equals(client.getProtocol())) {
            configuration.setProtocol(Protocol.HTTPS);
        }
        configuration.setUserAgent(client.getUserAgent());

        return configuration;
    }

    @Bean(name = "ossClient")
    public OSSClient ossClientForManager(OSSProperties oSSProperties, ClientConfiguration clientConfiguration) {
        ossClient = new OSSClient(oSSProperties.getEndpoint(), oSSProperties
                .getAccessKeyId(), oSSProperties.getAccessKeySecret(), clientConfiguration);
        return ossClient;
    }

    @Bean(name = "ossServiceFactory")
    public OSSServiceFactory ossServiceFactory() {
        return new OSSServiceFactory(new ALiOSSService(ossProperties.getBucketName(), ossClient));
    }
}
