package cn.jbinfo.reactor.log.sender;

import cn.jbinfo.reactor.log.event.ApiLogEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Rpc发送器
 *
 * @author xiaobin
 * @create 2017-08-30 下午1:22
 **/
@Component
public class ApiLogSender {

    @Async
    @EventListener
    public void execute(ApiLogEvent apiLogEvent) {
    }
}
