package cn.jbinfo.api.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author xiaobin
 * @create 2017-11-15 下午12:23
 **/
public interface PushLogRound {

    void before(HttpServletRequest request, Object handler);

    void after(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);
}
