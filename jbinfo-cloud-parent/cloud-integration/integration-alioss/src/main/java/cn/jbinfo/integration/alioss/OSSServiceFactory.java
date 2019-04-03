package cn.jbinfo.integration.alioss;

import cn.jbinfo.integration.alioss.model.OSSObject;
import cn.jbinfo.integration.alioss.model.ObjectResult;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author xiaobin
 * @create 2017-10-17 下午6:38
 **/
public class OSSServiceFactory {

    private OSSObjectServcie ossService;

    public void shutdown() {
        ossService.shutdown();
    }

    public OSSServiceFactory(OSSObjectServcie ossService) {
        this.ossService = ossService;
    }

    /**
     * 功能:保存oss对象
     */
    public String getAliOSSKey(String path) {
        return StringUtils.substringAfter(path, "/");
    }

    /**
     * 功能:保存oss对象
     *
     * @param key
     * @param file
     */
    public ObjectResult putAliOSSObject(String key, File file) {
        return ossService.putObject(key, file);
    }

    public String getAliOSSBucketName(String path) {
        return StringUtils.substringBefore(path, "/");
    }


    /**
     * 功能:保存oss对象
     *
     * @param key
     */
    public ObjectResult putAliOSSObject(String key, InputStream input) {
        return ossService.putObject(key, input);
    }

    /**
     * 功能:保存oss对象,带文件名
     *
     * @param key
     */
    public ObjectResult putAliOSSObject(String folder, String key, InputStream input) {
        return ossService.putObject(folder, key, input);
    }

    /**
     * 功能:保存oss对象,带文件名
     *
     * @param key
     * @param file
     */
    public ObjectResult putAliOSSObject(String folder, String key, File file) {
        return ossService.putObject(folder, key, file);
    }

    /**
     * 功能:保存oss对象
     *
     * @param key
     */
    public OSSObject getAliOSSObject(String key) {
        return ossService.getObject(key);
    }

    /**
     * 功能:删除oss对象
     *
     * @param key
     */
    public void delAliOSSObject(String key) {
        ossService.deleteObject(key);
    }

/*    public  void main(String[] args) {
//        System.out.println(getAliOSSKey("kipo-att-img/201605/527e842313da43428ed1bd75b06b1e5f.jpg"));
        OSSClient client = new OSSClient("oss-cn-shanghai.aliyuncs.com", "LTAIKVbvF3i7coWf", "mWSWc03ehnwh53JrGUZlJj0W9Viq82");
        OSSObjectServcie ossService = new ALiOSSService("phenix-att-test",client);
        ossService.putObject("1111", "2222", new File("/Users/xiaobin/demo/api-base-1.0.0.zip"));
    }*/
}
