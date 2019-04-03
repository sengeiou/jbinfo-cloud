package cn.jbinfo.api.annotation;

import java.lang.annotation.*;

/**
 * 方法注解
 * <p>
 * 不验证是否登录，忽略当前方法。
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
	/**
	 * 执行动作
	 * {@link Action}
	 */
	Action action() default Action.Normal;
}
