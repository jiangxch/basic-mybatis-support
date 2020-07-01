package com.github.jiangxch.mybatis.core.mapper;

import com.github.jiangxch.mybatis.core.annotations.AddEntityParam;
import com.github.jiangxch.mybatis.core.annotations.FillResultMap;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jiangxch
 * @date: 2020/5/13 下午10:47
 */
public interface SelectMapper<T> {
    @SelectProvider(type = CurdProvider.class, method = "findAll")
    @FillResultMap
    @AddEntityParam
    List<T> findAll();

    @SelectProvider(type = CurdProvider.class, method = "findById")
    @FillResultMap
    @AddEntityParam
    T findById(Serializable id);

    @SelectProvider(type = CurdProvider.class, method = "countAll")
    @AddEntityParam
    int countAll();
}
