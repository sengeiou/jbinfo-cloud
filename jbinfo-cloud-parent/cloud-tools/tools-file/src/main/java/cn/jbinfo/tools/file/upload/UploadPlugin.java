package cn.jbinfo.tools.file.upload;


import java.io.InputStream;

/**
 * 上传插件
 * Created by xiaobin on 16/6/30.
 */
public abstract class UploadPlugin {

    //存储模式
    public abstract StoreType getStoreType();

    //获取文件路径
    public abstract ObjectValue getFileUrl(InputStream file, String key, String ext);
}
