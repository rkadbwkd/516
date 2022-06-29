package com.anylogic.demo.common.excel.service;

import java.lang.String;
import java.lang.reflect.Field;
import org.springframework.aot.beans.factory.BeanDefinitionRegistrar;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ReflectionUtils;

public final class ContextBootstrapInitializer {
  public static void registerExcelSVC(DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("com.anylogic.demo.common.excel.service.ExcelSVC", ExcelSVC.class)
        .instanceSupplier((instanceContext) -> {
          ExcelSVC bean = new ExcelSVC();
          instanceContext.field("atcFileDir", String.class)
              .invoke(beanFactory, (attributes) -> {
                Field atcFileDirField = ReflectionUtils.findField(ExcelSVC.class, "atcFileDir", String.class);
                ReflectionUtils.makeAccessible(atcFileDirField);
                ReflectionUtils.setField(atcFileDirField, bean, attributes.get(0));
              });
                  return bean;
                }).register(beanFactory);
          }
        }
