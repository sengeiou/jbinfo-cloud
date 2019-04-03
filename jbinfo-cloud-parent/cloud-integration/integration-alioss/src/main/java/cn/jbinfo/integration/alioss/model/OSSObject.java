package cn.jbinfo.integration.alioss.model;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.Map;

/**
 * 对象存储后返回结果对象
 * @author xiaobin
 * @create 2017-10-17 下午6:23
 **/
@Getter
@Setter
public class OSSObject {

    private String key;

    /**
     * 对象元数据
     */
    private Map<String,Object> meatedata;

    /**
     * 自定义元数据
     */
    private Map<String, String> userMetadata;

    /**
     * 文件输入流
     */
    private InputStream objectConten;

}
