package com.github.jiangxch.mybatis.autoconfigure;

import com.github.jiangxch.mybatis.core.spring.DynamicDataSource;
import com.github.jiangxch.mybatis.core.support.DataSourceConfig;
import com.google.common.collect.Iterables;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * @author: jiangxch
 * @date: 2020/7/9 20:02
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@EnableConfigurationProperties(MybatisProperties.class)
public class MybatisAutoConfiguration implements ApplicationContextAware {

    @Autowired
    private MybatisProperties mybatisProperties;
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        String configLocation = "META-INF/mybatis/mybatis-config.xml";
        factoryBean.setConfigLocation(new ClassPathResource(configLocation));

        DataSourceConfig dsc = convertMybatisProperties2DataSourceConfig(mybatisProperties);

        factoryBean.setDataSource(DynamicDataSource.buildDruidDataSource(dsc));
        Resource[] mapperLocations = applicationContext.getResources("classpath:mybatis/mapper/*.xml");
        factoryBean.setMapperLocations(mapperLocations);
        factoryBean.setTypeHandlersPackage(mybatisProperties.getTypeHandlerPackage());
        return factoryBean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private DataSourceConfig convertMybatisProperties2DataSourceConfig(MybatisProperties mybatisProperties) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverClassName(mybatisProperties.getDriverClassName());
        dsc.setUrl(mybatisProperties.getUrl());
        dsc.setUsername(mybatisProperties.getUsername());
        dsc.setPassword(mybatisProperties.getPassword());
        return dsc;
    }
}
