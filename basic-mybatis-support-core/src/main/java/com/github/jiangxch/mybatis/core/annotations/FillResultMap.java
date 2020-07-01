package com.github.jiangxch.mybatis.core.annotations;

import javax.management.relation.Relation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mybatis 自定义provider时使用,
 * @FillResultMap 标示会把数据库的列值映射到entity对象上
 * 单个查或者列表查的provider方法需要加上,但是count之类的
 * 方法不需要加上
 *
 * @author: jiangxch
 * @date: 2020/7/1 下午10:47
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FillResultMap {
}
