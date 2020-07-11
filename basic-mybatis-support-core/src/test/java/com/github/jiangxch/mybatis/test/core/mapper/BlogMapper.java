package com.github.jiangxch.mybatis.test.core.mapper;

import com.github.jiangxch.mybatis.core.mapper.CurdProvider;
import com.github.jiangxch.mybatis.core.mapper.ICurdMapper;
import com.github.jiangxch.mybatis.test.core.entity.Blog;
import org.apache.ibatis.annotations.Update;


/**
 * @author: jiangxch
 * @date: 2020/5/10 下午7:52
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
