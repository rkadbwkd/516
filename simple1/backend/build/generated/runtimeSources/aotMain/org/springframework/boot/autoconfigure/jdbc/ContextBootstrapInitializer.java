package org.springframework.boot.autoconfigure.jdbc;

import java.lang.String;
import javax.sql.DataSource;
import org.springframework.aot.beans.factory.BeanDefinitionRegistrar;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public final class ContextBootstrapInitializer {
  public static void registerJdbcTemplateConfiguration(DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.jdbc.JdbcTemplateConfiguration", JdbcTemplateConfiguration.class)
        .instanceSupplier(JdbcTemplateConfiguration::new).register(beanFactory);
  }

  public static void registerJdbcTemplateConfiguration_jdbcTemplate(
      DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("jdbcTemplate", JdbcTemplate.class).withFactoryMethod(JdbcTemplateConfiguration.class, "jdbcTemplate", DataSource.class, JdbcProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(JdbcTemplateConfiguration.class).jdbcTemplate(attributes.get(0), attributes.get(1)))).customize((bd) -> {
      bd.setPrimary(true);
      bd.setDependsOn(new String[] { "dataSourceScriptDatabaseInitializer" });
    }).register(beanFactory);
  }

  public static void registerNamedParameterJdbcTemplateConfiguration(
      DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.jdbc.NamedParameterJdbcTemplateConfiguration", NamedParameterJdbcTemplateConfiguration.class)
        .instanceSupplier(NamedParameterJdbcTemplateConfiguration::new).register(beanFactory);
  }

  public static void registerNamedParameterJdbcTemplateConfiguration_namedParameterJdbcTemplate(
      DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("namedParameterJdbcTemplate", NamedParameterJdbcTemplate.class).withFactoryMethod(NamedParameterJdbcTemplateConfiguration.class, "namedParameterJdbcTemplate", JdbcTemplate.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(NamedParameterJdbcTemplateConfiguration.class).namedParameterJdbcTemplate(attributes.get(0)))).customize((bd) -> {
      bd.setPrimary(true);
      bd.setDependsOn(new String[] { "dataSourceScriptDatabaseInitializer" });
    }).register(beanFactory);
  }

  public static void registerDataSourceTransactionManagerAutoConfiguration_JdbcTransactionManagerConfiguration(
      DefaultListableBeanFactory beanFactory) {
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration$JdbcTransactionManagerConfiguration", DataSourceTransactionManagerAutoConfiguration.JdbcTransactionManagerConfiguration.class)
        .instanceSupplier(DataSourceTransactionManagerAutoConfiguration.JdbcTransactionManagerConfiguration::new).register(beanFactory);
  }
}
