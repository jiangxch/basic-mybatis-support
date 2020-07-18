package com.github.jiangxch.mybatis.autoconfig.test.dao;

import com.github.jiangxch.mybatis.autoconfig.test.dao.entity.Blog;
import com.github.jiangxch.mybatis.core.mapper.ICurdMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author: jiangxch
 * @date: 2020/7/16 下午11:28
 */
public interface BlogMapper extends ICurdMapper<Blog> {
    @Update("CREATE TABLE IF NOT EXISTS blog (\n" +
            "  id          INT PRIMARY KEY AUTO_INCREMENT NOT NULL,\n" +
            "  author      VARCHAR(60)                    NOT NULL,\n" +
            "  content     TEXT                           NOT NULL,\n" +
            "  access_date TIMESTAMP\n" +
            ");")
    void createTable();
}
