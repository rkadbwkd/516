package com.anylogic.demo.common.file.service;

import com.anylogic.demo.common.excel.service.ExcelSVC;
import com.anylogic.demo.common.file.mapper.AttachFileEmkoDemoMapper;
import java.lang.reflect.Field;
import org.springframework.aot.beans.factory.BeanDefinitionRegistrar;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ReflectionUtils;

public final class ContextBootstrapInitializer {
  public static void registerAttachFileEmkoDemoService(DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("com.anylogic.demo.common.file.service.AttachFileEmkoDemoService", AttachFileEmkoDemoService.class)
        .instanceSupplier((instanceContext) -> {
          AttachFileEmkoDemoService bean = new AttachFileEmkoDemoService();
          instanceContext.field("attachFileMapper", AttachFileEmkoDemoMapper.class)
              .invoke(beanFactory, (attributes) -> {
                Field attachFileMapperField = ReflectionUtils.findField(AttachFileEmkoDemoService.class, "attachFileMapper", AttachFileEmkoDemoMapper.class);
                ReflectionUtils.makeAccessible(attachFileMapperField);
                ReflectionUtils.setField(attachFileMapperField, bean, attributes.get(0));
              });
                  instanceContext.field("excelSVC", ExcelSVC.class)
                      .invoke(beanFactory, (attributes) -> {
                        Field excelSVCField = ReflectionUtils.findField(AttachFileEmkoDemoService.class, "excelSVC", ExcelSVC.class);
                        ReflectionUtils.makeAccessible(excelSVCField);
                        ReflectionUtils.setField(excelSVCField, bean, attributes.get(0));
                      });
                          return bean;
                        }).register(beanFactory);
                  }
                }
