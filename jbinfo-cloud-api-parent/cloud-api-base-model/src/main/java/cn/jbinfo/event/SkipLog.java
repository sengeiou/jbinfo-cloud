package cn.jbinfo.event;

import java.lang.annotation.*;

/**
 * @author xiaobin
 * @create 2017-09-02 下午3:50
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SkipLog {

    boolean skip() default true;
}
