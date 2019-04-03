package cn.jbinfo.tools.file;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author xiaobin
 * @create 2017-10-18 下午1:26
 **/
@ConfigurationProperties(prefix = "upload")
@Getter
@Setter
public class FileProperties {

    private String baseDir;

    private Map<String, String> pluginMap = Maps.newConcurrentMap();
}
