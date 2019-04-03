package cn.jbinfo.integration.alioss.impl;

import cn.jbinfo.cloud.core.utils.SpringContextHolder;
import cn.jbinfo.common.utils.DateUtils;
import cn.jbinfo.integration.alioss.OSSObjectServcie;
import cn.jbinfo.integration.alioss.model.OSSObject;
import cn.jbinfo.integration.alioss.model.ObjectResult;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.InputStream;

/**
 * 功能:阿里云OSS service实现类
 *
 * @author xiaobin
 * @create 2017-10-17 下午6:30
 **/
public class ALiOSSService implements OSSObjectServcie {
    /**
     * bucket名称
     */
    private String bucketName;

    private OSSClient client = null;

    public OSSClient getClient() {
        if (client == null)
            client = SpringContextHolder.getBean(OSSClient.class);
        return client;
    }

    /*
    public ALiOSSService() {
        this(OSSConfig.DEFAULT_BUCKET_NAME);
    }
*/

    public ALiOSSService(String bucketName, OSSClient client) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new IllegalArgumentException("aliyun bucket name is not Empty");
        }
        this.bucketName = bucketName;
        this.client = client;
    }

    /**
     * 带bucket构造方法
     *
     * @param bucketName bucket名称
     */
    public ALiOSSService(String bucketName) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new IllegalArgumentException("aliyun bucket name is not Empty");
        }
        this.bucketName = bucketName;
    }

    @Override
    public ObjectResult putObject(String key, File file) {
        if (file == null) {
            throw new IllegalArgumentException("object file is not null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("object file is not exists");
        }
        String dir = DateUtils.getDate("yyyyMM/");
        PutObjectResult putResult = getClient().putObject(bucketName, dir + key, file);
        return new ObjectResult(putResult.getETag(), putResult.getServerCRC());
    }

    @Override
    public void deleteObject(String key) {
        getClient().deleteObject(bucketName, key);
    }

    @Override
    public OSSObject getObject(String key) {
        com.aliyun.oss.model.OSSObject object = getClient().getObject(bucketName, key);
        //关闭连接
        return converObj(object);
    }

    @Override
    public OSSObject getObject(String bucketName, String key) {
        com.aliyun.oss.model.OSSObject object = getClient().getObject(bucketName, key);
        return converObj(object);
    }

    /**
     * 功能:关闭oss客户端
     */
    @Override
    public void shutdown() {
        if (client != null) {
            client.shutdown();
            client = null;
        }
    }

    private OSSObject converObj(com.aliyun.oss.model.OSSObject object) {
        OSSObject result = new OSSObject();
        result.setKey(object.getKey());
        result.setUserMetadata(object.getObjectMetadata().getUserMetadata());
        result.setObjectConten(object.getObjectContent());
        object = null;
        return result;

    }

    @Override
    public ObjectResult putObject(String key, InputStream input) {
        if (input == null) {
            throw new IllegalArgumentException("object file is not null");
        }
        PutObjectResult putResult = getClient().putObject(bucketName, key, input);
        //关闭连接
        return new ObjectResult(putResult.getETag(), putResult.getServerCRC());
    }

    @Override
    public ObjectResult putObject(String folder, String key,
                                  InputStream input) {
        if (StringUtils.isEmpty(folder)) {
            throw new IllegalArgumentException("OSS putObject folder is not Empty");
        }
        PutObjectResult putResult = getClient().putObject(bucketName, folder + key, input);
        //关闭连接
        return new ObjectResult(putResult.getETag(), putResult.getServerCRC());
    }

    @Override
    public ObjectResult putObject(String folder, String key, File file) {
        if (StringUtils.isEmpty(folder)) {
            throw new IllegalArgumentException("OSS putObject folder is not Empty");
        }
        PutObjectResult putResult = getClient().putObject(bucketName, folder + key, file);
        //关闭连接
        return new ObjectResult(putResult.getETag(), putResult.getServerCRC());
    }

    /**
     * 功能:保存一个对象需要手动关闭连接,一般用与批量循环操作
     *
     * @param folder
     * @param key
     * @param file
     * @return
     */
    @Override
    public ObjectResult putObjectClose(String folder, String key, File file) {
        if (StringUtils.isEmpty(folder)) {
            throw new IllegalArgumentException("OSS putObject folder is not Empty");
        }
        PutObjectResult putResult = getClient().putObject(bucketName, folder + key, file);
        return new ObjectResult(putResult.getETag(), putResult.getServerCRC());
    }
}
