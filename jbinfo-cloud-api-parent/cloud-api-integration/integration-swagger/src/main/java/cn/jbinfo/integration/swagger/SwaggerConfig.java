package cn.jbinfo.integration.swagger;

import cn.jbinfo.integration.swagger.version.Api1;
import cn.jbinfo.integration.swagger.version.Api2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;

/**
 * The type Swagger config.
 *
 * @author zhangxd
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket api1() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("版本1")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api1.class))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalDate.class, String.class)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo_1());
    }

    @Bean
    public Docket api2() {
        return new Docket(DocumentationType.SPRING_WEB).groupName("版本2")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api2.class))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalDate.class, String.class)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo_2());
    }

    /**
     * api info
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo_1() {
        return new ApiInfoBuilder()
                .title("平台API文档")//大标题
                .description("平台API文档")//详细描述
                .version("1.0")//版本
                .build();
    }

    private ApiInfo apiInfo_2() {
        return new ApiInfoBuilder()
                .title("平台API文档")//大标题
                .description("平台API文档")//详细描述
                .version("2.0")//版本
                .build();
    }


}
