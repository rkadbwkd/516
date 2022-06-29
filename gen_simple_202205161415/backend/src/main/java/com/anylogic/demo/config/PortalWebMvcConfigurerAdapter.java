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

import java.util.Arrays;
import java.util.List;

import com.anylogic.demo.common.mvc.message.MessageArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import static java.util.Arrays.asList;


@Configuration
public class PortalWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//            .allowedOrigins("http://localhost:8080");
            .allowedOrigins("*");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/").addResourceLocations("/resources/**");
        //외부 저장소 경로  
        registry.addResourceHandler("/upload/**").addResourceLocations("file:///C:/upload/").setCachePeriod(3600*24);
        //프로젝트 Image 경로 Tip 프로젝트 이미지 경로는 저장된 이미지 실행시 위에 찍히는 경로여야한다.
        //registry.addResourceHandler("/upload/**").addResourceLocations("file:///C:/Users/any9/Documents/workspace-spring-tool-suite-4-4.2.1.RELEASE/any-template_Demo2/src/main/webapp/upload/").setCachePeriod(3600*24);
        
    }

    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new MessageArgumentResolver());
        
        // super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.removeIf(v->v.getSupportedMediaTypes().contains(MediaType.APPLICATION_JSON)); // 기존 json용 컨버터 제거
        messageConverters.add(new MappingJackson2HttpMessageConverter()); // 새로 json 컨버터 추가. 필요시 커스텀 컨버터 bean 사용

        // MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
        // messageConverters.add(converter);

        // super.configureMessageConverters(messageConverters);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        /*registry.jsp("/WEB-INF/views/", ".jsp");
        super.configureViewResolvers(registry);*/
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        registry.viewResolver(resolver);
    }

    /*@Bean(name = "messageSource")
    public MessageSource configureMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(5);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }*/

    /**
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        mappings.put("org.springframework.dao.DataAccessException", "error");
        resolver.setExceptionMappings(mappings);
        return resolver;
    }
    */

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();
            resolver.setOrder(0);
            resolver.setPrefix("/WEB-INF/jsp/");
            resolver.setSuffix(".jsp");
            resolver.setViewClass(JstlView.class);
            return resolver;
    }
    
    @Bean
    public UrlBasedViewResolver getUrlBasedViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        JstlView jstlView = new JstlView();
        resolver.setOrder(1);
        resolver.setViewClass(jstlView.getClass());
            resolver.setPrefix("/WEB-INF/jsp/");
            resolver.setSuffix(".jsp");
            return resolver;
    }
}                


