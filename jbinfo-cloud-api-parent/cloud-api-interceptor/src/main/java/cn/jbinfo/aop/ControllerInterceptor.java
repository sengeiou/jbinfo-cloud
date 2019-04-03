package cn.jbinfo.aop;

import cn.jbinfo.api.annotation.Action;
import cn.jbinfo.api.annotation.Login;
import cn.jbinfo.api.context.InterfaceContextManager;
import cn.jbinfo.api.context.info.FilterLogRule;
import cn.jbinfo.api.utils.ApiTokenUtils;
import cn.jbinfo.bean.ApiVersion;
import cn.jbinfo.common.utils.DateUtils;
import cn.jbinfo.common.utils.JsonUtils;
import cn.jbinfo.event.Event;
import cn.jbinfo.event.SkipLog;
import cn.jbinfo.message.bo.LogDeviceEvent;
import cn.jbinfo.message.bo.LogInterface;
import cn.jbinfo.reactor.log.event.EventLogEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * controller过滤器
 *
 * @author xiaobin
 * @create 2017-09-02 上午8:56
 **/
@Aspect
public class ControllerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerInterceptor.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    //@Pointcut("execution(* cn.jbinfo.rpc.api.*.controller.*Controller.*(..))")
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        Object target = pjp.getTarget();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        String methodName = method.getName(); //获取被拦截的方法名
        LogInterface logInterface = InterfaceContextManager.getInterface();
        if (logInterface != null) {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();
            String url = request.getRequestURL().toString();
            String requestMethod = request.getMethod();
            //logInterface.setControllerName(target.getClass().getName());
            Api api = target.getClass().getAnnotation(Api.class);
            if (api != null) {
                logInterface.setApiName(api.value());
            }
            //logInterface.setMethodName(method.getName());
            logInterface.setUrl(url);
            logInterface.setRequestMethod(requestMethod);

            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            if (apiOperation != null) {
                //logInterface.setMethodInfo(apiOperation.value());
                logInterface.setApiName(logInterface.getApiName() + "#" + apiOperation.value());
            }
        }
        //跳过日志
        SkipLog skipLog = target.getClass().getAnnotation(SkipLog.class);
        LOGGER.info("targetClass:" + target.getClass() + ",skipLog:" + skipLog);
        if (skipLog != null && skipLog.skip()) {
            InterfaceContextManager.setFilterLog(FilterLogRule.ALL);
            return pjp.proceed();
        }
        Object[] args = pjp.getArgs();
        ApiVersion apiVersion = target.getClass().getAnnotation(ApiVersion.class);
        /*if (args.length < 1) {//参数小于1直接报错，说明无版本号
            throw new ApiException("无版本号信息");
        }*/
        String version;
        LOGGER.info("target: {} , method: {} , methodName: {} ", target, method, methodName);
        if (apiVersion != null) {
            version = "v" + apiVersion.value();
        } else if (args[0] instanceof String) {
            version = args[0].toString();
        } else {
            return pjp.proceed();
        }
        //-------------------------------拦截带版本号的接口，并加入到日志队列中--------------------------
        doEventBefore(method, args, version);
        doApiBefore(method, target, args, version);
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        doEventAfter();
        doApiAfter(result);
        LOGGER.info("请求结束，controller的返回值是 " + result);
        return result;
    }

    /**
     * 事件(经过此层的事件都会有接口的Id)
     *
     * @param method
     * @param args
     */
    private void doEventBefore(Method method, Object[] args, String apiVersion) {
        LOGGER.info("-------------doEventBefore---------------");
        LogInterface logInterface = InterfaceContextManager.getInterface();
        LogDeviceEvent deviceEvent;
        //-----------------------do event-----------------------------------

        Event event = method.getAnnotation(Event.class);
        //事件记录
        if (event != null && !event.skip()) {
            deviceEvent = new LogDeviceEvent();
            deviceEvent.setStartTime(DateUtils.getDateTime());
            deviceEvent.setApiId(logInterface.getId());
            deviceEvent.setEventEnum(event.value().getCode());

            //查看是否需要登录
            Login login = method.getAnnotation(Login.class);
            //跳过登录的接口，获取不到token。
            //做啥处理呢？？？
            if (login != null && login.action() == Action.Skip) {
            } else {
                deviceEvent.setToken(ApiTokenUtils.getToken().getValue());
                //记录事件制造者
                deviceEvent.setDeviceInfo(ApiTokenUtils.getDeviceUser());
            }
            deviceEvent.setApiVersion(apiVersion);
            InterfaceContextManager.setRestContextDeviceEvent(deviceEvent);
        } else {
            InterfaceContextManager.setFilterLog(FilterLogRule.EVENT);
        }
    }

    /**
     * 事件（）
     */
    private void doEventAfter() {
        if (InterfaceContextManager.getFilterLog() != null && InterfaceContextManager.getFilterLog() == FilterLogRule.EVENT)
            return;
        LogDeviceEvent event = InterfaceContextManager.getDeviceEvent();
        //LOGGER.info("-------------doEventAfter---------------");
        if (event == null)
            return;
        //LOGGER.info("-------------doEventAfter-------------2--");
        event.setEndTime(DateUtils.getDateTime());

        publisher.publishEvent(new EventLogEvent(this, event));
    }

    /**
     * 接口（之前执行）
     *
     * @param method
     * @param target
     * @param args
     */
    private void doApiBefore(Method method, Object target, Object[] args, String apiVersion) {
        LogInterface logInterface = InterfaceContextManager.getInterface();
        Event event = method.getAnnotation(Event.class);
        //事件记录接口版本
        if (event != null && !event.skipApi()) {//接口记录
            //接口參數
            //String apiVersion = (String) args[0];

            //查看是否需要登录
            Login login = method.getAnnotation(Login.class);
            //跳过登录的接口，获取不到token。
            //做啥处理呢？？？
            if (login != null && login.action() == Action.Skip) {
            } else {
                logInterface.setToken(ApiTokenUtils.getToken().getValue());
                //记录事件制造者
                logInterface.setDeviceInfo(ApiTokenUtils.getDeviceUser());
            }
            logInterface.setApiVersion(apiVersion);
            logInterface.setApiParamsJson(JsonUtils.safeObjectToJson(args));
        } else {
            InterfaceContextManager.setFilterLog(FilterLogRule.INTERFACE);
        }
    }

    private void doApiAfter(Object result) {
        LogInterface logInterface = InterfaceContextManager.getInterface();
        logInterface.setReturnJson(JsonUtils.safeObjectToJson(result));
    }

}
