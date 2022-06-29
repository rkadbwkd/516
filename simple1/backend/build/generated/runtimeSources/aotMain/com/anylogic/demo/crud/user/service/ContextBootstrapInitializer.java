package com.anylogic.demo.crud.user.service;

import com.anylogic.demo.crud.user.mapper.UserMapper;
import java.lang.reflect.Field;
import org.springframework.aot.beans.factory.BeanDefinitionRegistrar;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ReflectionUtils;

public final class ContextBootstrapInitializer {
  public static void registerUserServiceImpl(DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("com.anylogic.demo.crud.user.service.UserServiceImpl", UserServiceImpl.class)
        .instanceSupplier((instanceContext) -> {
          UserServiceImpl bean = new UserServiceImpl();
          instanceContext.field("userMapper", UserMapper.class)
              .invoke(beanFactory, (attributes) -> {
                Field userMapperField = ReflectionUtils.findField(UserServiceImpl.class, "userMapper", UserMapper.class);
                ReflectionUtils.makeAccessible(userMapperField);
                ReflectionUtils.setField(userMapperField, bean, attributes.get(0));
              });
                  return bean;
                }).register(beanFactory);
          }
        }
