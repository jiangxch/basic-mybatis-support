package com.github.jiangxch.mybatis.core.mapper;


/**
 * 基础的CURD mapper,让继承的子类自动具备curd功能
 * @author: jiangxch
 * @date: 2020/5/10 下午11:13
 */
public interface ICurdMapper<T> extends
        SelectMapper<T>,
        DeleteMapper<T>,
        InsertMapper<T>
{


}
