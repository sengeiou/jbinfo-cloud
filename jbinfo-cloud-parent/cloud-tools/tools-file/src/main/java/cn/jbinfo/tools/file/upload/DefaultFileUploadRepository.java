package cn.jbinfo.tools.file.upload;

import cn.jbinfo.tools.file.FileProperties;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by xiaobin268 on 2014-9-19.
 */
public class DefaultFileUploadRepository extends AbstractFileUploadRepository<MultipartFile> implements ServletContextAware {

    private Logger log = LoggerFactory.getLogger(DefaultFileUploadRepository.class);

    public DefaultFileUploadRepository(FileProperties fileProperties) {
        super(fileProperties);
    }


    public FileModel storeByExt(MultipartFile file, String key) {
        return storeByExt(file, StoreType.SYSTEM.getName(), key, false, null, null);
    }

    /**
     * 存储附件
     *
     * @param file
     * @param storeType 存储类型
     * @param key
     * @return
     */
    public FileModel storeByExt(MultipartFile file, StoreType storeType, String key) {
        return storeByExt(file, storeType.getName(), key, false, null, null);
    }

    @Override
    public FileModel storeByExt(MultipartFile file, StoreType storeType, String key, String ext) {
        return null;
    }

    public FileModel storeByExt(MultipartFile file, String key, boolean cutImg, String resizeWidth, String resizeHeight) {
        return storeByExt(file, StoreType.SYSTEM.getName(), key, cutImg, resizeWidth, resizeHeight);
    }



    /**
     * 文件存储
     *
     * @param file
     * @param folderType
     * @return
     */
    public FileModel storeByExt(MultipartFile file, String folderType, String key, boolean cutImg, String resizeWidth, String resizeHeight) {
        FileModel fileModel = new FileModel();

        String origName = file.getOriginalFilename();
        String contentType = file.getContentType();
        String ext = FilenameUtils.getExtension(origName).toLowerCase(Locale.ENGLISH);
        if (!checkExt(ext, fileModel)) {
            return fileModel;
        }
        fileModel.setExt(ext);
        try {
            fileModel.setFolderType(folderType);
            fileModel.setKey(key);
            fileModel.setCutImg(cutImg);
            fileModel.setResizeWidth(resizeWidth);
            fileModel.setResizeHeight(resizeHeight);

            getFileUrl(file.getInputStream(),fileModel,file);

            fileModel.setContentType(contentType);
            fileModel.setFileName(origName);// 文件名称(带文件扩展名)
            fileModel.setFileSize(file.getSize());
            return fileModel;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void store(MultipartFile file, File dest) throws IOException {
        try {
            UploadUtils.checkDirAndCreate(dest.getParentFile());
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("Transfer file error when upload file", e);
            throw e;
        }
    }
    public File retrieve(String name) {
        return new File(ctx.getRealPath(name));
    }

    protected ServletContext ctx;

    public void setServletContext(ServletContext servletContext) {
        this.ctx = servletContext;
    }

}
