package cn.jbinfo.tools.file.upload;

/**
 * 附件上传接口
 * Created by xiaobin on 2016/10/29.
 */
public interface IFileUploadRepository<F> {

    String UPLOAD_PATH = "resources/www";

    Integer BUFFER_LENGTH = 1024 * 1024 * 10;


    FileModel storeByExt(F file, String key);

    FileModel storeByExt(F file, StoreType storeType, String key);

    FileModel storeByExt(F file, StoreType storeType, String key, String ext);

    FileModel storeByExt(F file, String key, boolean cutImg, String resizeWidth, String resizeHeight);

    void deleteToken(String key);
}
