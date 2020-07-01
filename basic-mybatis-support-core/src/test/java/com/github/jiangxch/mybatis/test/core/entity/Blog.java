package com.github.jiangxch.mybatis.test.core.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: jiangxch
 * @date: 2020/5/10 下午7:53
 */
@Data
public class Blog {
    private Integer      id;
    private String       author;
    private String       content;
    private Date accessDate;
}
