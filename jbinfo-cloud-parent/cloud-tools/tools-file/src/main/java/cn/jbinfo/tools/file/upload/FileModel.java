package cn.jbinfo.tools.file.upload;


import lombok.Getter;
import lombok.Setter;

/**
 * 文件上传存储类
 *
 * @author xiaobin
 */
@Getter
@Setter
public class FileModel {

    /**
     * 视频类型
     */
    public static final String VIDEO = ".wmv.wm.asf.asx.rm.rmvb.ra.ram.mpg.mpeg.mpe.vob.dat.mov.3gp.mp4.mp4v.m4v.mkv.avi.flv.f4v.mts";
    /**
     * 音频
     */
    public static final String AUDIO = ".mp3.mv";
    /**
     * 图片
     */
    public static final String PICTURE = ".jpg.jpeg.gif.png";
    /**
     * 文档
     */
    public static final String DOC = ".doc.xls.docx.xlsx.pdf";
    /**
     * 幻灯片
     */
    public static final String PPT = ".ppt.pptx.pps.ppsx";

    private String docId;

    /**
     * 附件ID，对应AttMain的主键
     */
    private String attId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 压缩以后图片路径
     */
    private String reSizePath;

    /**
     * 文件扩展名
     */
    private String ext;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 存储类型 返回绝对路径
     */
    private StoreType storeType;

    /**
     * 文档链接类型
     */
    private String contentType;

    /**
     * 附件的MD5码
     */
    private String token;

    /**
     * 是否显示图片
     */
    private String imgShow;

    /**
     * 文件的crc32值
     */
    private Long crc32;


    public String getImgShow() {
        if (PICTURE.contains(ext)) {
            return "show";
        }
        return imgShow;
    }

    public void setImgShow(String imgShow) {
        this.imgShow = imgShow;
    }


    private String folderType;
    private String key;
    private boolean cutImg;
    private String resizeWidth;
    private String resizeHeight;

    public void setObjectValue(ObjectValue objectValue){
        this.filePath = objectValue.getFileUrl();
        this.crc32 = objectValue.getCrc();
    }

}
