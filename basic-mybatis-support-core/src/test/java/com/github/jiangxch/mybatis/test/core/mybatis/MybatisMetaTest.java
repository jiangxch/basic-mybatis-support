package com.github.jiangxch.mybatis.test.core.mybatis;

import com.sun.org.apache.regexp.internal.RE;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.junit.Test;

/**
 * MetaObject,MetaClass学习
 *
 * @author: jiangxch
 * @date: 2020/5/14 下午11:07
 */
public class MybatisMetaTest {

    private static final ObjectFactory objectFactory = new DefaultObjectFactory();
    private static final ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Data
    @NoArgsConstructor
    public static class Person {
        private String name;
        private Integer age;
    }

    @Test
    public void testBean() {
        Person bean = new Person();
        MetaObject metaObject = MetaObject.forObject(bean, objectFactory, objectWrapperFactory, REFLECTOR_FACTORY);
        metaObject.setValue("name","ahahha");
        System.out.println(bean);
    }

    @Test
    public void testMetaClass() {
        MetaClass metaClass = MetaClass.forClass(Person.class, REFLECTOR_FACTORY);
        System.out.println(metaClass.getSetterType("name"));
        System.out.println(metaClass.getSetterType("age"));
    }
}
