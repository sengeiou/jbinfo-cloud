package cn.jbinfo.reactor.log.sender;

import cn.jbinfo.reactor.log.event.EventLogEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * @author xiaobin
 * @create 2017-09-15 下午1:49
 **/
public class EventLogSender {

    @Async
    @EventListener
    public void execute(EventLogEvent eventLogEvent) {
    }
}
