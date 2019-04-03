package cn.jbinfo.api.interceptor;

import cn.jbinfo.api.context.InterfaceContextManager;
import cn.jbinfo.api.context.info.FilterLogRule;
import cn.jbinfo.api.event.PushLogRound;
import cn.jbinfo.common.utils.DateUtils;
import cn.jbinfo.message.bo.LogInterface;
import cn.jbinfo.reactor.log.event.ApiLogEvent;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author xiaobin
 * @create 2017-11-15 下午1:31
 **/
public class PushLogRoundImpl implements PushLogRound {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushLogRoundImpl.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void before(HttpServletRequest request, Object handler) {
        InterfaceContextManager.start();
    }

    @Override
    public void after(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        FilterLogRule logRule = InterfaceContextManager.getFilterLog();
        LOGGER.info("logRule====" + logRule);
        if (logRule != null && (logRule == FilterLogRule.ALL || logRule == FilterLogRule.INTERFACE)) {
            return;
        }
        LogInterface logInterface = InterfaceContextManager.getInterface();
        if ("1".equals(logInterface.getIsError())) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            logInterface.setApiName(apiOperation.value() + "#" + request.getMethod());
            logInterface.setUrl(request.getRequestURL().toString());
        } else {
            logInterface.setIsError("0");
        }
        logInterface.setEndTime(DateUtils.getDateTime());
        //eventBus.notify(ConsumerConstants.SEND_API_LOG, Event.wrap(logInterface));
        publisher.publishEvent(new ApiLogEvent(this, logInterface));
    }
}
