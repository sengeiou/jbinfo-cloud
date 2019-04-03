package cn.jbinfo.conf;

import cn.jbinfo.aop.ControllerInterceptor;
import cn.jbinfo.api.event.PushLogRound;
import cn.jbinfo.api.interceptor.PushLogRoundImpl;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author xiaobin
 * @create 2017-11-15 下午1:32
 **/
@Configurable
public class InterceptorConf {

    @Bean
    public ControllerInterceptor controllerInterceptor() {
        return new ControllerInterceptor();
    }

    @Bean(name = "pushLogRound")
    public PushLogRound pushLogRound() {
        return new PushLogRoundImpl();
    }
}
