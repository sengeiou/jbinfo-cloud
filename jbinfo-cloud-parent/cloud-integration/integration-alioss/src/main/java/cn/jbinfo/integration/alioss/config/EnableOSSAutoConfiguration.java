package cn.jbinfo.integration.alioss.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 描述: aliyun oss 自动配置注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {OSSAutoConfiguration.class})
public @interface EnableOSSAutoConfiguration {
}
