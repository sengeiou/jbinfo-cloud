package cn.jbinfo.integration.alioss.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 对象存储后返回结果对象
 * @author xiaobin
 * @create 2017-10-17 下午6:22
 **/
@Getter
@Setter
public class ObjectResult {
    /**
     * put完对象返回的标签
     */
    private String tag;
    private Long crc;

    public ObjectResult(String tag,Long crc) {
        this.tag = tag;
        this.crc = crc;
    }

}
