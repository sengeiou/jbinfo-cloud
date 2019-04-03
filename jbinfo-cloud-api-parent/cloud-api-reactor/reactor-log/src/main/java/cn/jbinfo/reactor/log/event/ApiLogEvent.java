package cn.jbinfo.reactor.log.event;

import cn.jbinfo.message.bo.LogInterface;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @author xiaobin
 * @create 2017-09-18 下午4:17
 **/
@Getter
@Setter
public class ApiLogEvent extends ApplicationEvent {

    private LogInterface logInterface;

    public ApiLogEvent(Object source, LogInterface logInterface) {
        super(source);
        this.logInterface = logInterface;
    }
}
