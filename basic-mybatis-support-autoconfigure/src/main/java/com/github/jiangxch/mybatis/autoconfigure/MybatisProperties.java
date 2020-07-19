package com.github.jiangxch.mybatis.autoconfigure;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: jiangxch
 * @date: 2020/7/11 下午2:59
 */
@ConfigurationProperties(prefix = "mybatis")
@Data
public class MybatisProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String typeHandlerPackage;
}
