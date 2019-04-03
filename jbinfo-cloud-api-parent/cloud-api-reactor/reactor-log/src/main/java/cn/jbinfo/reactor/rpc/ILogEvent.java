package cn.jbinfo.reactor.rpc;

import cn.jbinfo.message.bo.LogDeviceEvent;
import cn.jbinfo.message.bo.LogDeviceHeartbeat;
import cn.jbinfo.message.bo.LogInterface;
import cn.jbinfo.message.bo.LogModel;

import java.util.List;

/**
 * @author xiaobin
 * @create 2017-11-23 下午6:13
 **/
public interface ILogEvent {

    String sendApiLog(List<LogInterface> apiLogList);

    String sendHeartBeat(List<LogDeviceHeartbeat> deviceHeartbeatList);

    String sendDeviceEvent(List<LogDeviceEvent> deviceEventList);

    /**
     * 日志补救策略
     *
     * @param logModel
     */
    String sendLogModel(LogModel logModel);
}
