package com.github.jiangxch.mybatis.core.spring;

import org.springframework.jdbc.datasource.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 读取配置文件动态刷新的 DataSource
 * @author: jiangxch
 * @date: 2020/7/11 下午2:31
 */
public class DynamicDataSource extends AbstractDataSource {

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }
}
