package cn.jbinfo.reactor.log.event;

import cn.jbinfo.message.bo.LogDeviceEvent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @author xiaobin
 * @create 2017-09-18 下午4:19
 **/
@Getter
@Setter
public class EventLogEvent extends ApplicationEvent {

    private LogDeviceEvent deviceEvent;

    public EventLogEvent(Object source,LogDeviceEvent deviceEvent) {
        super(source);
        this.deviceEvent = deviceEvent;
    }
}
