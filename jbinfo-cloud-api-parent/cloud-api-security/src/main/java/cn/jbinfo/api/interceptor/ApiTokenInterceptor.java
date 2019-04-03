package cn.jbinfo.api.interceptor;

import cn.jbinfo.api.constant.ApiCode;
import cn.jbinfo.api.constant.Resource;
import cn.jbinfo.api.context.InterfaceContextManager;
import cn.jbinfo.api.context.RestContextManager;
import cn.jbinfo.api.event.PushLogRound;
import cn.jbinfo.api.exception.ApiException;
import cn.jbinfo.cloud.core.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaobin
 * @create 2017-11-15 下午12:22
 **/
public class ApiTokenInterceptor extends TokenInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTokenInterceptor.class);

    private ApplicationEventPublisher publisher;

    public ApiTokenInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // access token
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String accessToken = request.getHeader(Resource.REQ_ACCESS_TOKEN);
        if (StringUtils.isBlank(accessToken))
            accessToken = request.getParameter(Resource.REQ_ACCESS_TOKEN);
        try{
            PushLogRound pushLogRound = SpringContextHolder.getBean("pushLogRound");
            if (pushLogRound != null)
                pushLogRound.before(request, handler);
        }catch (Exception ex){

        }
        if (!isNeedLogin(handler)) {
            InterfaceContextManager.setNeedLogin(false);
            return true;
        }
        if (StringUtils.isNotEmpty(accessToken)) {
            RestContextManager.initAccessToken(accessToken);
            return true;
        }
        throw new ApiException(ApiCode.AUTH_LESS);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            try{
                PushLogRound pushLogRound = SpringContextHolder.getBean("pushLogRound");
                if (pushLogRound != null)
                    pushLogRound.after(request, response, handler, ex);
            }catch (Exception e){

            }
        }
    }
}
