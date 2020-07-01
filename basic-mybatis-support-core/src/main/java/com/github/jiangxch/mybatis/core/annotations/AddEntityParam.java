package com.github.jiangxch.mybatis.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mybatis 自定义provider时使用,
 * @AddEntityParam 标示会把 Mapper 中的泛型参数传给 provider
 * 的方法参数中
 * @author: jiangxch
 * @date: 2020/7/1 下午11:59
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AddEntityParam {
}
