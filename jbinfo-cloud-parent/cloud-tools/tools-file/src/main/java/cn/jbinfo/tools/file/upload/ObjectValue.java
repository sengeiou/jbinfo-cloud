package cn.jbinfo.tools.file.upload;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaobin
 * @create 2017-10-18 下午2:31
 **/
@Getter
@Setter
public class ObjectValue {

    private String fileUrl;

    private Long crc;

    public ObjectValue() {
    }

    public ObjectValue(String fileUrl, Long crc) {
        this.fileUrl = fileUrl;
        this.crc = crc;
    }

}
