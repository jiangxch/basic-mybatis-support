package com.github.jiangxch.mybatis.autoconfig.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.sql.DataSource;

/**
 * @author: jiangxch
 * @date: 2020/7/15 上午12:39
 */
@SpringBootApplication
public class SpringBootTest {
    public static void main(String[] args) {
        System.setProperty("driverClassName", "org.h2.Driver");
        System.setProperty("username", "sa");
        System.setProperty("password", "");
        System.setProperty("url", "jdbc:h2:mem:blog;DB_CLOSE_DELAY=-1");


        ConfigurableApplicationContext run = SpringApplication.run(SpringBootTest.class, args);
        DataSource bean = run.getBean(DataSource.class);
        System.out.println(bean.getClass().getName());

    }
}
