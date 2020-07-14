package com.github.jiangxch.mybatis.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: jiangxch
 * @date: 2020/7/11 下午2:59
 */
@ConfigurationProperties(prefix = RowMybatisProperties.MYBATIS_PREFIX)
public class MybatisProperties extends RowMybatisProperties {

}
