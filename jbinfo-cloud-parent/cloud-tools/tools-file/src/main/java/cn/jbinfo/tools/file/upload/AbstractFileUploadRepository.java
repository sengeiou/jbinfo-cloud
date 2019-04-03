package cn.jbinfo.tools.file.upload;

import cn.jbinfo.cloud.core.utils.MyBeanUtils;
import cn.jbinfo.tools.file.FileProperties;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaobin on 2016/10/29.
 */
public abstract class AbstractFileUploadRepository<F> implements IFileUploadRepository<F> {

    private FileProperties fileProperties;

    public AbstractFileUploadRepository(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    protected void getFileUrl(InputStream inputStream, FileModel fileModel, F file)
            throws IOException {
        final String fileUrl;
        Collection<UploadPlugin> uploadPlugin = getPlugin();
        if (CollectionUtils.isEmpty(uploadPlugin)) {
            fileUrl = storeByExt(fileModel.getExt(), file, fileModel.getKey(), fileModel.isCutImg(), fileModel.getResizeWidth(), fileModel.getResizeHeight());
            Long crc32 = FileUtils.checksumCRC32(new File(fileUrl));
            fileModel.setCrc32(crc32);
            fileModel.setStoreType(StoreType.SYSTEM);
            fileModel.setFilePath(fileUrl);
        } else {
            uploadPlugin.forEach(plugin -> {
                fileModel.setObjectValue(plugin.getFileUrl(inputStream, fileModel.getKey(), fileModel.getExt()));
                fileModel.setStoreType(plugin.getStoreType());
            });
        }
        if (StringUtils.isBlank(fileModel.getFilePath())) {
            throw new RuntimeException("附件上传失败!fileUrl is not be null!");
        }
    }

    protected abstract void store(F file, File dest) throws IOException;

    protected String storeByExt(String ext, F file, String key, boolean cutImg, String resizeWidth, String resizeHeight) throws IOException {
        String destPath = fileProperties.getBaseDir();
        String defiled = destPath + UPLOAD_PATH;
        File dest = new File(defiled + File.separator + UploadUtils.MONTH_FORMAT.format(new Date()) + File.separator + key + "." + ext);
        store(file, dest);
        File tmp = new File(defiled + File.separator + UploadUtils.MONTH_FORMAT.format(new Date()) + File.separator + key);
        if (tmp.exists()) {
            tmp.delete();
        }
        /*if (ImageUtils.isValidImageExt(ext) && cutImg) {
            String outPath = defiled + File.separator + UploadUtils.MONTH_FORMAT.format(new Date()) + File.separator + key + "-reduction" + "." + ext;
            int width = NumberUtils.toInt(resizeWidth, NumberUtils.toInt(Global.getConfig("resizeFix.image.width"), 150));
            int height = NumberUtils.toInt(resizeHeight, NumberUtils.toInt(Global.getConfig("resizeFix.image.height"), 228));
            AverageImageScale.resizeFix(dest, new File(outPath), width, height);
            return outPath;
        }*/

        return dest.getAbsolutePath();
    }

    /**
     * 插件
     *
     * @return
     */
    protected Collection<UploadPlugin> getPlugin() {
        Map<String, String> uploadPluginMap = fileProperties.getPluginMap();
        if (MapUtils.isNotEmpty(uploadPluginMap)) {
            Map<String,UploadPlugin> pluginMap = Maps.newHashMap();
            uploadPluginMap.forEach((key,value)-> pluginMap.put(key, MyBeanUtils.newInstance(value)));
            return pluginMap.values();
        }
        return null;
    }

    /**
     * 根据附件标识删除附件
     *
     * @param key
     */
    public void deleteToken(String key) {
        if (key == null || key.isEmpty())
            return;
        String destPath = fileProperties.getBaseDir();
        String file = destPath + UPLOAD_PATH;
        File f = new File(file + File.separator + UploadUtils.MONTH_FORMAT.format(new Date()) + File.separator + key);
        if (f.exists())
            f.delete();
    }

    /**
     * 检测附件后缀
     *
     * @param ext
     * @param fileModel
     * @return
     */
    protected boolean checkExt(String ext, FileModel fileModel) {
        if ("jsp".equals(ext) || "php".equals(ext) || "html".equals(ext)
                || "htm".equals(ext) || "jspx".equals(ext)) {
            fileModel.setContentType("");
            fileModel.setExt("毛");
            fileModel.setFileName("y@@@@@@@@@@@@@@@");// 文件名称(带文件扩展名)
            fileModel.setFilePath("NNNNNNNNNNNNNNNNNNNNNNNNN");
            fileModel.setFileSize(0);
            return false;
        }
        return true;
    }


    /**
     * Acquired the file.
     *
     * @param key
     * @return
     * @throws FileNotFoundException If key not found, will throws this.
     */
    public File getTokedFile(String key) throws FileNotFoundException {
        if (key == null || key.isEmpty())
            return null;

        String destPath = fileProperties.getBaseDir();
        String file = destPath + UPLOAD_PATH;

        File f = new File(file + File.separator + UploadUtils.MONTH_FORMAT.format(new Date()) + File.separator + key);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            throw new FileNotFoundException("File `" + f + "` not exist.");

        return f;
    }

    /**
     * According the key, generate a file (if not exist, then create a new
     * file).
     * <p>
     * the file relative path(something like `a../bxx/wenjian.txt`)
     *
     * @return
     * @throws IOException
     */
    public File getFile(String key, String fileName) throws IOException {
        if (key == null || key.isEmpty())
            return null;
        String ext = FilenameUtils.getExtension(fileName);

        String destPath = fileProperties.getBaseDir();
        String file = destPath + UPLOAD_PATH;

        File f = new File(file + File.separator + UploadUtils.MONTH_FORMAT.format(new Date()) + File.separator + key + "." + ext);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();

        return f;
    }

    /**
     * From the InputStream, write its data to the given file.
     */
    public long streaming(InputStream in, String key, String fileName) throws IOException {
        OutputStream out = null;
        File f = getTokedFile(key);
        try {
            out = new FileOutputStream(f);

            int read = 0;
            final byte[] bytes = new byte[BUFFER_LENGTH];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        } finally {
            IOUtils.closeQuietly(out);
        }

        String ext = FilenameUtils.getExtension(fileName);
        String destPath = fileProperties.getBaseDir();
        String file = destPath + UPLOAD_PATH;

        File dst = new File(file + File.separator + UploadUtils.MONTH_FORMAT.format(new Date()) + File.separator + key + "." + ext);
        dst.delete();
        f.renameTo(dst);

        long length = getFile(key, fileName).length();
        return length;
    }

    static final Pattern RANGE_PATTERN = Pattern
            .compile("bytes \\d+-\\d+/\\d+");

    /**
     * 获取Range参数
     *
     * @param req
     * @return
     * @throws IOException
     */
    public Range parseRange(HttpServletRequest req) throws IOException {
        String range = req.getHeader("content-range");
        Matcher m = RANGE_PATTERN.matcher(range);
        if (m.find()) {
            range = m.group().replace("bytes ", "");
            String[] rangeSize = range.split("/");
            String[] fromTo = rangeSize[0].split("-");

            long from = Long.parseLong(fromTo[0]);
            long to = Long.parseLong(fromTo[1]);
            long size = Long.parseLong(rangeSize[1]);

            return new Range(from, to, size);
        }
        throw new IOException("Illegal Access!");
    }


    private void store(File file, File dest) throws IOException {
        try {
            UploadUtils.checkDirAndCreate(dest.getParentFile());
            FileUtils.copyFile(file, dest);
        } catch (IOException e) {
            throw e;
        }
    }

    public void storeToken(String key) throws IOException {
        if (key == null || key.isEmpty())
            return;
        String destPath = fileProperties.getBaseDir();
        String file = destPath + UPLOAD_PATH;

        File f = new File(file + File.separator + UploadUtils.MONTH_FORMAT.format(new Date()) + File.separator + key);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();
    }

   /* public String canConverImg(String key, String fileName, File dest, String resizeWidth, String resizeHeight) throws IOException {
        String ext = FilenameUtils.getExtension(fileName);
        if (ImageUtils.isValidImageExt(ext)) {
            String destPath = Global.getBaseDir();
            String file = destPath + UPLOAD_PATH;

            int width = NumberUtils.toInt(resizeWidth, NumberUtils.toInt(Global.getConfig("resizeFix.image.width"), 150));
            int height = NumberUtils.toInt(resizeHeight, NumberUtils.toInt(Global.getConfig("resizeFix.image.height"), 228));
            String outPath = file + File.separator + UploadUtils.MONTH_FORMAT.format(new Date()) + File.separator + key + "-" + width + "-" + height + "." + ext;
            AverageImageScale.resizeFix(dest, new File(outPath), width, height);
            return outPath;
        }
        return null;
    }*/


}
