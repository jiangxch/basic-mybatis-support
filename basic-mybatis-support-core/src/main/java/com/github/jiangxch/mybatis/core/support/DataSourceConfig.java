package com.github.jiangxch.mybatis.core.support;

import com.google.common.base.Strings;
import lombok.Getter;

/**
 * @author: jiangxch
 * @date: 2020/7/14 下午11:47
 */
@Getter
public class DataSourceConfig {
    private static final String DRIVER_CLASS_NAME_KEY = "driverClassName";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String URL = "url";

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public static DataSourceConfig getFromSystemProperties(){
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.driverClassName = System.getProperty(DRIVER_CLASS_NAME_KEY);
        dsc.username = System.getProperty(USERNAME_KEY);
        dsc.password = System.getProperty(PASSWORD_KEY);
        dsc.url = System.getProperty(URL);
        dsc.validate();
        return dsc;
    }

    private void validate() {
        if (Strings.isNullOrEmpty(driverClassName)) {
            throw new RuntimeException("[driverClassName] must not be null");
        }
        if (Strings.isNullOrEmpty(url)) {
            throw new RuntimeException("[url] must not be null");
        }
        if (Strings.isNullOrEmpty(username)) {
            throw new RuntimeException( "[username] must not be null");
        }
    }
}
