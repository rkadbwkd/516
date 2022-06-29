package org.springframework.boot.autoconfigure.gson;

import org.springframework.aot.beans.factory.BeanDefinitionRegistrar;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public final class ContextBootstrapInitializer {
  public static void registerGsonAutoConfiguration_standardGsonBuilderCustomizer(
      DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("standardGsonBuilderCustomizer", GsonAutoConfiguration.StandardGsonBuilderCustomizer.class).withFactoryMethod(GsonAutoConfiguration.class, "standardGsonBuilderCustomizer", GsonProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(GsonAutoConfiguration.class).standardGsonBuilderCustomizer(attributes.get(0)))).register(beanFactory);
  }
}
