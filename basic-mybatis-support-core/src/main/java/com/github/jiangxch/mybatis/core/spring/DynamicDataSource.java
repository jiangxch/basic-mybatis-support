package com.github.jiangxch.mybatis.core.spring;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.jiangxch.mybatis.core.support.DataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 读取配置文件动态刷新的 DataSource
 * @author: jiangxch
 * @date: 2020/7/11 下午2:31
 */

public class DynamicDataSource extends AbstractDataSource implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSource.class);

    private DruidDataSource druidDataSource;



    @Override
    public Connection getConnection() throws SQLException {
        return druidDataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return druidDataSource.getConnection(username,password);
    }

    public static DruidDataSource buildDruidDataSource() {
        long start = System.currentTimeMillis();
        DruidDataSource ds;
        ds = new DruidDataSource();
        DataSourceConfig dsc = DataSourceConfig.getFromSystemProperties();

        ds.setUrl(dsc.getUrl());
        ds.setUsername(dsc.getUsername());
        ds.setPassword(dsc.getPassword());
        ds.setDriverClassName(dsc.getDriverClassName());
        try {
            ds.setFilters("stat,mergeStat,slf4j");
        } catch (Exception e) {
            log.error("cannot add slf4j filter with {}", dsc.getUrl(), e);
        }
        ds.setMaxActive(100);
        ds.setInitialSize(0);
        ds.setMinIdle(0);
        ds.setMaxWait( 60000);
        ds.setTimeBetweenEvictionRunsMillis(3000);
        ds.setMinEvictableIdleTimeMillis( 300000);
        ds.setValidationQuery("SELECT 'x'");
        ds.setTestWhileIdle(true);
        ds.setTestOnReturn(false);
        ds.setTestOnBorrow(false);
        // 针对mysql关闭prepareStatement
        if (ds.getUrl().contains(":mysql:")) {
            ds.setPoolPreparedStatements(false);
        } else {
            ds.setPoolPreparedStatements(true);
            ds.setMaxPoolPreparedStatementPerConnectionSize(100);
        }
        try {
            ds.init();
        } catch (Exception e) {
            log.error("cannot init [{}]", dsc.getUrl(), e);
        } finally {
            long cost = System.currentTimeMillis() - start;
            log.info("build dataSource({}) url={}, cost {}ms", ds.getName(), dsc.getUrl(), cost);
        }
        return ds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.druidDataSource = buildDruidDataSource();
    }
}
