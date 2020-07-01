package com.github.jiangxch.mybatis.test.core;

import com.github.jiangxch.mybatis.test.core.entity.Blog;
import com.github.jiangxch.mybatis.test.core.mapper.BlogMapper;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 原生mybatis curd单元测试
 *
 * @author: jiangxch
 * @date: 2020/5/5 下午11:59
 */
public class CurdTest {
    private static final Logger log = LoggerFactory.getLogger(CurdTest.class);
    private static SqlSessionFactory sqlSessionFactory;
    private static int id = 0;

    @BeforeClass
    public static void beforeClass() {
        try (InputStream is = Resources.getResourceAsStream("mybatis/mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            initDb();
        } catch (IOException e) {
            log.error("can't load mybatis-config.xml, ", e);
        }
    }

    public static void initDb() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
            File f = Resources.getResourceAsFile("sql/blog.sql");
            List<String> lines = Files.readLines(f, Charset.defaultCharset());
//            log.info("read the lines is: \n{}", lines);
            // lines 每一刚通过 "\n" 拼接起来
            String sql = Joiner.on("\n").join(lines);

            PreparedStatement preparedStatement = sqlSession.getConnection().prepareStatement(sql);
            preparedStatement.executeUpdate();

        } catch (IOException e) {
            log.error("load sql/blog.sql failed, e", e);
        } catch (SQLException e) {
            log.error("can't execute sql, e", e);
        }
    }

    @Test
    public void testSelectById() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            insertData(sqlSession);
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            Blog entity = mapper.findById(id-1);
            Assert.assertEquals("bb"+(id-1), entity.getContent());
            mapper.deleteAll();
        }
    }

    @Test
    public void testSelectByCondition() {

    }

    @Test
    public void testSelectByPage() {

    }



    @Test
    public void testSelectAll() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            insertData(sqlSession);
            insertData(sqlSession);
            insertData(sqlSession);
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            List<Blog> all = mapper.findAll();
            Assert.assertEquals(all.size(),3);
            mapper.deleteAll();
        }
    }

    @Test
    public void testInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            insertData(sqlSession);
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            List<Blog> all = mapper.findAll();
            Assert.assertEquals(all.size(),1);
            Assert.assertEquals(all.get(0).getContent(),"bb" +(id-1));
            mapper.deleteAll();
        }
    }

    public void insertData(SqlSession sqlSession) {
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = new Blog();
        blog.setAuthor("aa"+id);
        blog.setId(id);
        blog.setContent("bb"+id);
        blog.setAccessDate(new Date());
        mapper.insert(blog);
        id++;
    }

    @Test
    public void testCountAll() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            insertData(sqlSession);
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            Assert.assertEquals(1,mapper.countAll());
            mapper.deleteAll();
        }
    }

    @Test
    public void testDeleteAll(){
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            insertData(sqlSession);
            insertData(sqlSession);
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            Assert.assertEquals(2,mapper.countAll());
            mapper.deleteAll();
            Assert.assertEquals(0,mapper.countAll());
        }
    }

    @Test
    public void testDeleteById(){
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            insertData(sqlSession);
            insertData(sqlSession);
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            Assert.assertEquals("bb"+(id-1),mapper.findById(id-1).getContent());
            mapper.deleteById(id-1);
            Assert.assertNull(mapper.findById(id-1));
        }
    }
}
