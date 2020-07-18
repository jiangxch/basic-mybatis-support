package com.github.jiangxch.mybatis.autoconfig.test.daotest;
import java.util.Date;

import com.github.jiangxch.mybatis.autoconfig.test.dao.BlogMapper;
import com.github.jiangxch.mybatis.autoconfig.test.dao.entity.Blog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: jiangxch
 * @date: 2020/7/16 下午11:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogMapperTest {
    static {
        System.setProperty("driverClassName", "org.h2.Driver");
        System.setProperty("username", "sa");
        System.setProperty("password", "");
        System.setProperty("url", "jdbc:h2:mem:blog;DB_CLOSE_DELAY=-1");
        System.setProperty("configLocation", "com.github.jiangxch.mybatis.autoconfig.test.dao");
    }

    @Autowired
    private BlogMapper blogMapper;
    @Test
    public void test(){
        blogMapper.createTable();
        System.out.println(blogMapper.findAll());

        Blog blog = new Blog();
        blog.setId(0);
        blog.setAuthor("aa");
        blog.setContent("bbb");
        blog.setAccessDate(new Date());

        blogMapper.insert(blog);
        System.out.println(blogMapper.findAll());
    }
}
