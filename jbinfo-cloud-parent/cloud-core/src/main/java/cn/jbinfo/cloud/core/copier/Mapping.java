package cn.jbinfo.cloud.core.copier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A(filed)-->B(filed)
 * A(name) -->B(_name)
 * Created by xiaobin on 2016/11/2.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Mapping {

    String value() default "";

}