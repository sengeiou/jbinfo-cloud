package cn.jbinfo.api.context.info;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 接口开始日志
 *
 * @author xiaobin
 * @create 2017-08-27 下午6:48
 **/
@Getter
@Setter
@ToString
@Deprecated
public class StartLog implements Serializable {

    private String controllerName;

    private String methodName;

    private String controllerInfo;

    private String methodInfo;

    //请求方式
    private String requestMethod;

    public StartLog() {

    }

    public StartLog(HttpServletRequest request, Object handler) {
        requestMethod = request.getMethod();

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        controllerName = handlerMethod.getBean().getClass().getName();
        Api api = handlerMethod.getBean().getClass().getAnnotation(Api.class);
        if (api != null) {
            controllerInfo = api.value();
        }
        methodName = handlerMethod.getMethod().getName();
        ApiOperation apiOperation = handlerMethod.getMethod().getAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            methodInfo = apiOperation.value();
        }
    }

}
