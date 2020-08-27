package com.kang.codetool.util;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.Contact;

@EnableOpenApi
@Configuration
public class Swagger3Config {

    @Bean
    public Docket createRestApi() {
        //返回文档摘要信息
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    //生成接口信息，包括标题、联系人等
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger3接口文档")
                .description("接口文档")
                .contact(new Contact("康志兴", "https://www.github.com/kangzhixing/", "522454279@qq.com"))
                .version("1.0")
                .build();
    }
}
//
//import io.swagger.annotations.Api;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
///**
// * Swagger2配置
// * http://localhost:18005/swagger-ui.html
// * http://localhost:18005/doc.html
// */
//@Configuration
//@EnableSwagger2
//class SwaggerConfig extends WebMvcConfigurerAdapter {
//    /**
//     * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
//     */
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
////                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
//                .apis(RequestHandlerSelectors.basePackage("com.kang.codetool.controller"))
////为当前包路径
////.apis(RequestHandlerSelectors.basePackage("com.br.demo.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    /**
//     * 构建 api文档的详细信息函数
//     */
//    private ApiInfo apiInfo() {
//        ApiInfo apiInfo = new ApiInfoBuilder()
////页面标题
//                .title("code API")
////创建人
//                .contact(new Contact("kangzhixing", "", ""))
////版本号
//                .version("1.0")
////描述
//                .description("code API")
//                .build();
//        return apiInfo;
//    }
//
//    // 继承WebMvcConfigurerAdapter，指定静态文件路径
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
//}
