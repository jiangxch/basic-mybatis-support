package com.github.jiangxch.mybatis.core.mapper;

import com.github.jiangxch.mybatis.core.annotations.AddEntityParam;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: jiangxch
 * @date: 2020/5/13 下午10:47
 */
public interface DeleteMapper<T> {

    /**
     * 通过delete语句删除表中所有数据,不会释放
     *
     * @return
     */
    @DeleteProvider(type = CurdProvider.class, method = "deleteAll")
    @AddEntityParam
    int deleteAll();

    /**
     * truncate 表,无事物,无法恢复,但不占用空间
     *
     * @return
     */
    @DeleteProvider(type = CurdProvider.class, method = "truncate")
    @AddEntityParam
    int truncate();

    @DeleteProvider(type = CurdProvider.class, method = "deleteById")
    @AddEntityParam
    int deleteById(Serializable id);
}
