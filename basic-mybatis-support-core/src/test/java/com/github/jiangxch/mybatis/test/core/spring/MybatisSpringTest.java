package com.github.jiangxch.mybatis.test.core.spring;

import com.github.jiangxch.mybatis.test.core.entity.Blog;
import com.github.jiangxch.mybatis.test.core.mapper.BlogMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * @author: jiangxch
 * @date: 2020/7/11 上午11:54
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class MybatisSpringTest {
    @Autowired
    private BlogMapper blogMapper;

    @PostConstruct
    public void init(){
        blogMapper.createTable();
        blogMapper.truncate();
    }

    @Test
    public void te(){
        Blog blog = new Blog();
        blog.setId(1);
        blog.setAuthor("aaa");
        blog.setContent("bbb");
        blog.setAccessDate(new Date());

        blogMapper.insert(blog);
        System.out.println(blogMapper.findAll());
    }
}
