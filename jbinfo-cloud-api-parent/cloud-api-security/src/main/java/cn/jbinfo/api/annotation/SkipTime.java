package cn.jbinfo.api.annotation;

import java.lang.annotation.*;

/**
 * Created by xiaobin on 2016/11/8.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SkipTime {
}
