package cn.jbinfo.integration.alioss.plugin;

import cn.jbinfo.cloud.core.utils.SpringContextHolder;
import cn.jbinfo.common.utils.DateUtils;
import cn.jbinfo.integration.alioss.OSSServiceFactory;
import cn.jbinfo.integration.alioss.config.OSSProperties;
import cn.jbinfo.integration.alioss.model.ObjectResult;
import cn.jbinfo.tools.file.FileProperties;
import cn.jbinfo.tools.file.upload.ObjectValue;
import cn.jbinfo.tools.file.upload.StoreType;
import cn.jbinfo.tools.file.upload.UploadPlugin;
import cn.jbinfo.tools.file.upload.UploadUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * @author xiaobin
 * @create 2017-10-18 下午2:01
 **/
public class OssUploadPlugin extends UploadPlugin {

    private Logger log = LoggerFactory.getLogger(OssUploadPlugin.class);

    @Override
    public StoreType getStoreType() {
        return StoreType.OSS;
    }

    @Override
    public ObjectValue getFileUrl(InputStream file, String key, String ext) {
        //文件相对路径
        String dir = DateUtils.getDate("yyyyMM/");
        OSSProperties ossProperties = SpringContextHolder.getBean(OSSProperties.class);
        FileProperties fileProperties = SpringContextHolder.getBean(FileProperties.class);
        //bucket-name.endpoint /
        String website = ossProperties.getWebsite();
        if (StringUtils.isBlank(website)) {
            website = ossProperties.getEndpoint();
        }
        String fileUrl = "http://" + ossProperties.getBucketName() + "." + website + "/" + dir + key + "." + ext;
        String defile = fileProperties.getBaseDir();
        ObjectValue objectValue = new ObjectValue();
        try {
            OSSServiceFactory ossServiceFactory = SpringContextHolder.getBean(OSSServiceFactory.class);
            ObjectResult objectResult = ossServiceFactory.putAliOSSObject(dir, key + "." + ext, file);
            log.info(objectResult.getTag());
            objectValue.setCrc(objectResult.getCrc());
        } catch (Exception e) {
            log.error("上传附件到OSS异常", e);
            throw new RuntimeException("上传到Oss发生异常:", e);
        }
        //删除临时文件
        File tmp = new File(defile + File.separator + UploadUtils.MONTH_FORMAT.format(new Date()) + File.separator + key);
        if (tmp.exists()) {
            tmp.deleteOnExit();
        }
        objectValue.setFileUrl(fileUrl);
        return objectValue;
    }
}
