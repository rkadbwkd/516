package com.anylogic.demo.common.file.controller;

import com.anylogic.demo.common.file.service.AttachFileEmkoDemoService;
import java.lang.reflect.Field;
import org.springframework.aot.beans.factory.BeanDefinitionRegistrar;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ReflectionUtils;

public final class ContextBootstrapInitializer {
  public static void registerAttachFileEmkoDemoController(DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("com.anylogic.demo.common.file.controller.AttachFileEmkoDemoController", AttachFileEmkoDemoController.class)
        .instanceSupplier((instanceContext) -> {
          AttachFileEmkoDemoController bean = new AttachFileEmkoDemoController();
          instanceContext.field("attachFileService", AttachFileEmkoDemoService.class)
              .invoke(beanFactory, (attributes) -> {
                Field attachFileServiceField = ReflectionUtils.findField(AttachFileEmkoDemoController.class, "attachFileService", AttachFileEmkoDemoService.class);
                ReflectionUtils.makeAccessible(attachFileServiceField);
                ReflectionUtils.setField(attachFileServiceField, bean, attributes.get(0));
              });
                  return bean;
                }).register(beanFactory);
          }
        }
