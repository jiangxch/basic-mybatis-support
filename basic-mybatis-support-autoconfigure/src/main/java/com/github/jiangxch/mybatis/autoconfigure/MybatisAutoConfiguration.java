package com.github.jiangxch.mybatis.autoconfigure;

import com.github.jiangxch.mybatis.core.spring.DynamicDataSource;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiangxch
 * @date: 2020/7/9 20:02
 */
@Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
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
        if (mybatisProperties.getConfigLocation() != null
                && mybatisProperties.getConfigLocation().length() != 0) {
            configLocation = mybatisProperties.getConfigLocation();
        }
        factoryBean.setConfigLocation(new ClassPathResource(configLocation));
        factoryBean.setDataSource(DynamicDataSource.buildDruidDataSource());
        factoryBean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());

        Resource[] mapperLocations = applicationContext.getResources("classpath:mybatis/mapper/*.xml");
        factoryBean.setMapperLocations(mapperLocations);
        return factoryBean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
