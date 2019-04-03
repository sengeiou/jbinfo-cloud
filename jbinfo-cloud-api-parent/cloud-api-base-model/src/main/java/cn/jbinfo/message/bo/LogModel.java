package cn.jbinfo.message.bo;

import cn.jbinfo.cloud.core.utils.IdGen;
import cn.jbinfo.cloud.core.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 防止数据丢失
 *
 * @author xiaobin
 * @create 2017-11-24 上午10:41
 **/
@Getter
@Setter
public class LogModel implements Serializable {

    public LogModel() {
        this.id = IdGen.uuid();
    }

    public void setHeartBeatModel(List<LogDeviceHeartbeat> deviceHeartbeatList) {
        this.logType = "DEVICE_HEART_BEAT";
        this.logJson = JsonUtils.safeObjectToJson(deviceHeartbeatList);
    }

    public void setDeviceEventModel(List<LogDeviceEvent> deviceEventList) {
        this.logType = "DEVICE_EVENT";
        this.logJson = JsonUtils.safeObjectToJson(deviceEventList);
    }

    public void setApiLogModel(List<LogInterface> logInterfaceList) {
        this.logType = "API_LOG";
        this.logJson = JsonUtils.safeObjectToJson(logInterfaceList);
    }

    //消息Id
    private String id;

    /**
     * DEVICE_EVENT
     * DEVICE_HEART_BEAT
     * API_LOG
     */
    private String logType;

    private String logJson;
}
