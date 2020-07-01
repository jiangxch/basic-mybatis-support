package com.github.jiangxch.mybatis.core.support;

import lombok.Data;
import org.apache.ibatis.mapping.ResultMap;

import java.util.List;

/**
 * 定义mybatis泛型类相关属性,与EntityMeta不同
 * @author: jiangxch
 * @date: 2020/5/14 下午11:42
 */
@Data
public class MapperMeta {
    private Class<?> entityClass;
    private List<ResultMap> resultMaps;
    /**
     * 有该注解的方法，fillResultMap标志为true,标示映射属性
     * {@link com.github.jiangxch.mybatis.core.annotations.FillResultMap}
     */
    private boolean fillResultMap;
    /**
     * 有该注解的方法，fillResultMap标志为true,标示映射属性
     * {@link com.github.jiangxch.mybatis.core.annotations.AddEntityParam}
     */
    private boolean addEntityParam;
}