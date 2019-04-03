package cn.jbinfo.tools.file.upload;

import cn.jbinfo.tools.file.FileProperties;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * @author xiaobin
 * @create 2017-10-19 下午1:41
 **/
public class CommonFileUploadRepository extends AbstractFileUploadRepository<FileItem> implements ServletContextAware {

    private Logger log = LoggerFactory.getLogger(CommonFileUploadRepository.class);

    public CommonFileUploadRepository(FileProperties fileProperties) {
        super(fileProperties);
    }

    @Override
    public FileModel storeByExt(FileItem file, String key) {
        return storeByExt(file, StoreType.SYSTEM.getName(), key, false, null, null, null);
    }

    @Override
    public FileModel storeByExt(FileItem file, StoreType storeType, String key) {
        return storeByExt(file, storeType.getName(), key, false, null, null, null);
    }

    @Override
    public FileModel storeByExt(FileItem file, StoreType storeType, String key, String ext) {
        return storeByExt(file, storeType.getName(), key, false, null, null, ext);
    }

    @Override
    public FileModel storeByExt(FileItem file, String key, boolean cutImg, String resizeWidth, String resizeHeight) {
        return storeByExt(file, StoreType.SYSTEM.getName(), key, cutImg, resizeWidth, resizeHeight, null);
    }

    /**
     * 文件存储
     *
     * @param file
     * @param folderType
     * @return
     */
    public FileModel storeByExt(FileItem file, String folderType, String key,
                                boolean cutImg, String resizeWidth, String resizeHeight, String fileExt) {
        FileModel fileModel = new FileModel();
        String contentType = file.getContentType();
        String origName = file.getName();
        String ext = FilenameUtils.getExtension(origName).toLowerCase(
                Locale.ENGLISH);
        if (StringUtils.isBlank(ext))
            ext = fileExt;
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
            getFileUrl(file.getInputStream(), fileModel, file);
            fileModel.setContentType(contentType);
            fileModel.setFileName(origName);// 文件名称(带文件扩展名)
            fileModel.setFileSize(file.getSize());
            return fileModel;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void store(FileItem file, File dest) throws IOException {
        try {
            UploadUtils.checkDirAndCreate(dest.getParentFile());
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("Transfer file error when upload file", e);
            throw e;
        } catch (Exception e) {
            log.error("Transfer file error when upload file", e);
            throw new IOException(e);
        }
    }

    protected ServletContext ctx;

    public void setServletContext(ServletContext servletContext) {
        this.ctx = servletContext;
    }
}
