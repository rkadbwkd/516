/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.function.Predicate;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
//@EnableSwagger2
//@Import({BeanValidatorPluginsConfiguration.class, SpringDataRestConfiguration.class})
//@ComponentScan(basePackageClasses = {
//    SampleController.class
//})
public class SwaggerConfig {

    @Bean
    public Docket defaultApiDocket() {
//        return new Docket(DocumentationType.SWAGGER_2)
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
//                .useDefaultResponseMessages(false)
//                .ignoredParameterTypes(AuthenticationPrincipal.class)
                //노출 제외 처리
//                .paths(Predicates.not(PathSelectors.regex("/(error|actuator).*")))
                .build()
                    .pathMapping("/swagger")
                    .apiInfo(getApiInfo())
                    .useDefaultResponseMessages(false)
                //.securitySchemes(Collections.singletonList(securitySchema()))
                //.securityContexts(Collections.singletonList(securityContext()))
                ;
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Anylogic RESTful USER",
                "Demo Api",
                "1.0.0",
                "/swagger",
                new Contact("Anilogic", "http://anylogic.co.kr", "hianpro@anylogic.co.kr"),
                null,
                null,
                Collections.emptyList()
        );
    }

    private Predicate<String> paths() {
        return regex("^\"/(error|actuator).*\"");
    }

}


