package com.anylogic.demo.crud.user.controller;

import com.anylogic.demo.crud.user.service.UserService;
import java.lang.reflect.Field;
import org.springframework.aot.beans.factory.BeanDefinitionRegistrar;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ReflectionUtils;

public final class ContextBootstrapInitializer {
  public static void registerUserController(DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("com.anylogic.demo.crud.user.controller.UserController", UserController.class)
        .instanceSupplier((instanceContext) -> {
          UserController bean = new UserController();
          instanceContext.field("userService", UserService.class)
              .invoke(beanFactory, (attributes) -> {
                Field userServiceField = ReflectionUtils.findField(UserController.class, "userService", UserService.class);
                ReflectionUtils.makeAccessible(userServiceField);
                ReflectionUtils.setField(userServiceField, bean, attributes.get(0));
              });
                  return bean;
                }).register(beanFactory);
          }
        }
