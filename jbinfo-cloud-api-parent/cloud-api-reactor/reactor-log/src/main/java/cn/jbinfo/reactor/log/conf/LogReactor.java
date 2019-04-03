package cn.jbinfo.reactor.log.conf;

import cn.jbinfo.reactor.log.sender.ApiLogSender;
import cn.jbinfo.reactor.log.sender.EventLogSender;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author xiaobin
 * @create 2017-09-15 下午8:14
 **/
@Configurable
public class LogReactor {

    @Bean
    ApiLogSender apiLogSender() {
        return new ApiLogSender();
    }

    @Bean
    EventLogSender eventLogSender() {
        return new EventLogSender();
    }

}
