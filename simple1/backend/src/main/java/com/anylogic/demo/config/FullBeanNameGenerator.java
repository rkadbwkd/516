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

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FullBeanNameGenerator implements org.springframework.beans.factory.support.BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
//        return getFullName((AnnotatedBeanDefinition) definition);
//        return definition.getBeanClassName();
        return definition.getBeanClassName();
    }

    private String getFullName(final AnnotatedBeanDefinition definition) {
        // 패키지를 포함한 전체 이름을 반환한다.
        return definition.getMetadata().getClassName();
    }
}


