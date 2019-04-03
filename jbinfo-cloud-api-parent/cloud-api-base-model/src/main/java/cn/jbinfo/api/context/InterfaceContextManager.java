package cn.jbinfo.api.context;

import cn.jbinfo.api.base.RestResponse;
import cn.jbinfo.api.context.info.AuthLog;
import cn.jbinfo.api.context.info.ErrorLog;
import cn.jbinfo.api.context.info.FilterLogRule;
import cn.jbinfo.api.context.info.ParamLog;
import cn.jbinfo.cloud.core.utils.IdGen;
import cn.jbinfo.cloud.core.utils.MyBeanUtils;
import cn.jbinfo.common.utils.DateUtils;
import cn.jbinfo.common.utils.JsonUtils;
import cn.jbinfo.message.bo.LogDeviceEvent;
import cn.jbinfo.message.bo.LogInterface;

import java.util.Map;

/**
 * 接口日志记录
 *
 * @author xiaobin
 * @create 2017-08-27 下午6:32
 **/
public class InterfaceContextManager {

    //日誌過濾器相關
    private static final ThreadLocal<FilterLogRule> REST_CONTEXT_FILTER_LOG = new ThreadLocal<>();


    //接口日志
    private static final ThreadLocal<LogInterface> REST_CONTEXT_INTERFACE = new ThreadLocal<>();

    //设备事件
    private static final ThreadLocal<LogDeviceEvent> REST_CONTEXT_DEVICE_EVENT = new ThreadLocal<>();

    public static void setRestContextDeviceEvent(LogDeviceEvent deviceEvent) {
        REST_CONTEXT_DEVICE_EVENT.set(deviceEvent);
    }

    public static FilterLogRule getFilterLog() {
        return REST_CONTEXT_FILTER_LOG.get();
    }

    public static void setFilterLog(FilterLogRule rule) {
        FilterLogRule filterLogRule = getFilterLog();
        if (filterLogRule != null) {
            if (filterLogRule != rule) {
                REST_CONTEXT_FILTER_LOG.set(FilterLogRule.ALL);
            }
        } else {
            REST_CONTEXT_FILTER_LOG.set(rule);
        }
    }

    public static LogDeviceEvent getDeviceEvent() {
        if (REST_CONTEXT_DEVICE_EVENT.get() == null) {
            return new LogDeviceEvent();
        }
        return REST_CONTEXT_DEVICE_EVENT.get();
    }

    public static void setDeviceEventResource(String resourceId,String resourceName){
        if (REST_CONTEXT_DEVICE_EVENT.get() == null) {
            return;
        }
        LogDeviceEvent logDeviceEvent = REST_CONTEXT_DEVICE_EVENT.get();
        logDeviceEvent.setResourceId(resourceId);
        logDeviceEvent.setResourceName(resourceName);
        REST_CONTEXT_DEVICE_EVENT.set(logDeviceEvent);
    }

    public static void setInterface(LogInterface context) {
        REST_CONTEXT_INTERFACE.set(context);
    }

    public static LogInterface getInterface() {
        if (REST_CONTEXT_INTERFACE.get() == null) {
            return new LogInterface();
        }
        return REST_CONTEXT_INTERFACE.get();
    }

    //接口启动
    public static void start() {
        LogInterface logInterface = new LogInterface();
        logInterface.setId(IdGen.uuid());
        logInterface.setStartTime(DateUtils.getDateTime());
        setInterface(logInterface);
    }

    //认证信息
    public static void auth(AuthLog authLog) {
        buildLog(authLog);
        if (getDeviceEvent() != null) {
            getDeviceEvent().setDeviceInfo(authLog.getDeviceInfo());
            getDeviceEvent().setToken(authLog.getToken());
        }
    }

    public static void setNeedLogin(Boolean isNeedLogin) {
        LogInterface logInterface = getInterface();
        if (logInterface == null)
            return;
        logInterface.setIsNeedLogin(isNeedLogin);
        setInterface(logInterface);
    }

    //参数相关
    public static void param(ParamLog paramLog) {
        buildLog(paramLog);
    }

    public static void returnParam(RestResponse restResponse) {
        LogInterface logInterface = getInterface();
        if (logInterface == null)
            return;
        logInterface.setReturnJson(JsonUtils.safeObjectToJson(restResponse));
        setInterface(logInterface);
    }

    public static void returnParam(String json) {
        LogInterface logInterface = getInterface();
        if (logInterface == null)
            return;
        logInterface.setReturnJson(json);
        setInterface(logInterface);
    }

    //参数相关
    public static void error(ErrorLog errorLog) {
        buildLog(errorLog);
    }


    private static void buildLog(Object bean) {
        LogInterface logInterface = getInterface();
        if (logInterface == null)
            return;
        Map<String, Object> map = MyBeanUtils.describe(bean);
        for (String key : map.keySet()) {
            MyBeanUtils.setFieldValue(logInterface, key, map.get(key));
        }
        setInterface(logInterface);
    }
}
