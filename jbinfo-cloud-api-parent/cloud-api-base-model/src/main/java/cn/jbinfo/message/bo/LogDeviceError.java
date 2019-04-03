package cn.jbinfo.message.bo;

import cn.jbinfo.rpc.api.model.DeviceInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 終端異常
 *
 * @author xiaobin
 * @create 2017-08-29 上午11:09
 **/
@Getter
@Setter
@ToString
public class LogDeviceError implements Serializable {

    private static final long serialVersionUID = -6570396454949179975L;

    public static final String COLLECTION_NAME = "LOG_DEVICE_ERROR_V2";

    @Id
    private String id;

    private String eventCode;

    private String eventBody;

    private String errorDescribe;

    private String createTime;

    //会话token
    private String token;
    //設備信息
    private DeviceInfo deviceInfo;

    //接口版本
    private String apiVersion;
}
