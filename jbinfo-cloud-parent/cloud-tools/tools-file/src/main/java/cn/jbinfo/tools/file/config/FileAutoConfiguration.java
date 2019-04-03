package cn.jbinfo.tools.file.config;

import cn.jbinfo.tools.file.FileProperties;
import cn.jbinfo.tools.file.upload.CommonFileUploadRepository;
import cn.jbinfo.tools.file.upload.DefaultFileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaobin
 * @create 2017-10-18 下午1:34
 **/
@Configuration
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {

    @Autowired
    FileProperties fileProperties;

    @Bean("defaultFileUploadRepository")
    public DefaultFileUploadRepository defaultFileUploadRepository(FileProperties fileProperties){
        return new DefaultFileUploadRepository(fileProperties);
    }

    @Bean("commonFileUploadRepository")
    public CommonFileUploadRepository commonFileUploadRepository(FileProperties fileProperties){
        return new CommonFileUploadRepository(fileProperties);
    }
}
