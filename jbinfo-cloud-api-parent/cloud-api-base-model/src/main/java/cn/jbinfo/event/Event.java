package cn.jbinfo.event;

import java.lang.annotation.*;

/**
 * 事件描述
 *
 * @author xiaobin
 * @create 2017-09-02 上午10:19
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Event {

    EventEnum value() default EventEnum.NULL;

    //是否跳过事件记录
    //针对日志接口（只记录接口信息）
    boolean skip() default false;

    //跳过接口记录
    boolean skipApi() default false;
}
