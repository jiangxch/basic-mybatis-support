package com.github.jiangxch.mybatis.core.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jiangxch
 * @date: 2020/5/13 下午10:47
 */
public interface InsertMapper<T> {
    @InsertProvider(type = CurdProvider.class, method = "insert")
    int insert(T entity);
}
