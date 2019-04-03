package cn.jbinfo.conf;

import cn.jbinfo.api.context.cache.TokenCache;
import cn.jbinfo.api.interceptor.ApiTokenInterceptor;
import cn.jbinfo.bean.CustomRequestMappingHandlerMapping;
import cn.jbinfo.cloud.core.utils.SpringContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

/**
 * WEB配置类
 *
 * @author xiaobin
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        //registry.excludePathPatterns("/error");
        //addInterceptor.excludePathPatterns("/swagger**");
        //addInterceptor.addPathPatterns("/**");
        registry.addInterceptor(new ApiTokenInterceptor()).addPathPatterns("/api/**");
        super.addInterceptors(registry);
    }


    @Autowired
    private ApplicationEventPublisher publisher;

    @Bean
    public TokenCache getTokenCache() {
        return new TokenCache();
    }

/*
    @Bean
    public ControllerInterceptor controllerInterceptor() {
        return new ControllerInterceptor();
    }*/

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false) // 系统对外暴露的 URL 不会识别和匹配 .* 后缀
                .setUseTrailingSlashMatch(true); // 系统不区分 URL 的最后一个字符是否是斜杠 /
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        // 等价于<mvc:default-servlet-handler />, 对静态资源文件的访问, 将无法 mapping 到 Controller 的 path 交给 default servlet handler 处理
        configurer.enable();
    }

    /**
     * Validator local validator factory bean.
     *
     * @return the local validator factory bean
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * Gets method validation post processor.
     *
     * @return the method validation post processor
     */
    @Bean
    public MethodValidationPostProcessor getMethodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(validator());
        return processor;
    }

    @Override
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping mapping = new CustomRequestMappingHandlerMapping();
        mapping.setOrder(0);
        mapping.setInterceptors(getInterceptors());
        mapping.setContentNegotiationManager(mvcContentNegotiationManager());
        mapping.setCorsConfigurations(getCorsConfigurations());

        PathMatchConfigurer configurer = getPathMatchConfigurer();
        Boolean useSuffixPatternMatch = configurer.isUseSuffixPatternMatch();
        Boolean useRegisteredSuffixPatternMatch = configurer.isUseRegisteredSuffixPatternMatch();
        Boolean useTrailingSlashMatch = configurer.isUseTrailingSlashMatch();
        mapping.setUseSuffixPatternMatch(useSuffixPatternMatch);
        if (useRegisteredSuffixPatternMatch != null)
            mapping.setUseRegisteredSuffixPatternMatch(useRegisteredSuffixPatternMatch);
        mapping.setUseTrailingSlashMatch(useTrailingSlashMatch);

        UrlPathHelper pathHelper = configurer.getUrlPathHelper();
        if (pathHelper != null)
            mapping.setUrlPathHelper(pathHelper);

        PathMatcher pathMatcher = configurer.getPathMatcher();
        if (pathMatcher != null)
            mapping.setPathMatcher(pathMatcher);
        return mapping;
    }

    @Bean
    public SpringContextHolder getSpringContextHolder() {
        return new SpringContextHolder();
    }
}
