package cn.jbinfo.integration.alioss;

import cn.jbinfo.integration.alioss.model.OSSObject;
import cn.jbinfo.integration.alioss.model.ObjectResult;

import java.io.File;
import java.io.InputStream;

/**
 * 功能:OSS 对象操作 Service 接口类
 * @author xiaobin
 * @create 2017-10-17 下午6:28
 **/
public interface OSSObjectServcie {

    /**
     * 功能:把对象put到OSS服务器
     *
     * @param key  对象key
     * @param file 文件
     * @return
     */
    ObjectResult putObject(String key, File file);

    /**
     * 功能:把对象put到OSS服务器
     * @return
     */
    ObjectResult putObject(String key, InputStream input);

    /**
     * 功能:把对象put到OSS服务器,带文件名需要以/结尾
     * @return
     */
    ObjectResult putObject(String folder, String key, File file);


    /**
     * 功能:把对象put到OSS服务器,带文件名需要以/结尾
     * @return
     */
    ObjectResult putObject(String folder, String key, InputStream input);

    /**
     * 功能:把对象put到OSS服务器
     * @return
     */
    void deleteObject(String key);

    /**
     * 功能:获取一个对象信息
     * @return
     */
    OSSObject getObject(String key);

    /**
     * 功能:保存一个对象需要手动关闭连接,一般用与批量循环操作
     */
    ObjectResult putObjectClose(String folder, String key, File file);


    /**
     * 功能:关闭oss客户端
     */
    void shutdown();

    /**
     * 功能:后去指定buket中的对象
     */
    OSSObject getObject(String buketName, String key);
}
