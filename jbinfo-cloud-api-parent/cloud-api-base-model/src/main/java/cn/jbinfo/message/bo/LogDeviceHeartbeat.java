package cn.jbinfo.message.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 设备心跳
 *
 * @author xiaobin
 * @create 2017-08-28 下午5:43
 **/
@Getter
@Setter
@ToString
public class LogDeviceHeartbeat implements Serializable {

    private static final long serialVersionUID = -5793207459702433968L;

    public static final String COLLECTION_NAME = "LOG_DEVICE_HEART_BEAT_V2";

    @Id
    private String id;

    //心跳时间
    private String heartBeatTime;

    //上一次心跳时间
    private String lastHeartBeatTime;

    //回话token
    private String token;

    private String deviceId;

    //房间号
    private String roomId;

    //终端版本
    private String appVersion;

    private String tenantId;

    private String tenantName;

    //说明
    private String describe;
}
