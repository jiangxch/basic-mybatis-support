package com.github.jiangxch.mybatis.autoconfigure;

import com.github.jiangxch.mybatis.core.spring.DynamicDataSource;
import com.google.common.collect.Iterables;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: jiangxch
 * @date: 2020/7/9 20:02
 */
@Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class })
public class MybatisAutoConfiguration {

    @Autowired
    private MybatisProperties mybatisProperties;
    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(){
        DynamicDataSource ds = new DynamicDataSource();
        return ds;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DataSource.class)
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        String configLocation = "classpath*:mybatis/basic-mybatis-support-config.xml";
        if (mybatisProperties.getConfigLocation() != null
                && mybatisProperties.getConfigLocation().length() != 0) {
            configLocation = mybatisProperties.getConfigLocation();
        }

        factoryBean.setConfigLocation(new ClassPathResource(configLocation));
        factoryBean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        List<Resource> mapperLocations = new ArrayList<>();
        if (mybatisProperties.getMapperLocations() != null) {
            for (String mapperLocation : mybatisProperties.getMapperLocations()) {
                mapperLocations.add(new ClassPathResource(mapperLocation));
            }
        }
        factoryBean.setMapperLocations(Iterables.toArray(mapperLocations,Resource.class));
        return factoryBean;
    }
}
