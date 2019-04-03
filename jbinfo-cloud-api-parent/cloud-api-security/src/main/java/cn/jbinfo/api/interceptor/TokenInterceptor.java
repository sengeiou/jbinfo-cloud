package cn.jbinfo.api.interceptor;

import cn.jbinfo.api.annotation.Action;
import cn.jbinfo.api.annotation.Login;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.lang.reflect.Method;

/**
 * Created by xiaobin on 2017/8/17.
 */
public abstract class TokenInterceptor extends HandlerInterceptorAdapter {

    protected boolean isNeedLogin(Object handler){
        if (handler instanceof HandlerMethod) {
            //验证登录
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Login login = method.getAnnotation(Login.class);
            if (login != null) {
                if (login.action() == Action.Skip) {
                    return false;
                }
            }
        }
        return true;
    }
}
