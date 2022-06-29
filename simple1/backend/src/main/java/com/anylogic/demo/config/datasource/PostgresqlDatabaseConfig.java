/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.anylogic.demo.config.CustomBeanNameGenerator;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.anylogic", annotationClass = Mapper.class, sqlSessionFactoryRef = "postgreSqlSessionFactory", nameGenerator = CustomBeanNameGenerator.class)
@EnableTransactionManagement
public class PostgresqlDatabaseConfig {

    @Bean(name = "postgreDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource postgreDataSource(){
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "postgreSqlSessionFactory")
    @Primary
    public SqlSessionFactory postgreSqlSessionFactory(@Qualifier("postgreDataSource")DataSource postgreDataSource, ApplicationContext applicationContext) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(postgreDataSource);
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/config/mybatis-config.xml"));
        sqlSessionFactoryBean.setMapperLocations((new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/**/*.xml")));
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);  // Spring Boot 전용 VFS 사용하도록 지정

        return  sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "postgreSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate postgreSqlSessionTemplate(SqlSessionFactory postgreSqlSessionFactory) {
        return new SqlSessionTemplate(postgreSqlSessionFactory);
    }

    @Bean
    @Primary
    public DataSourceTransactionManager postgreSqlTransactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(postgreDataSource()); return manager;
    }

}


