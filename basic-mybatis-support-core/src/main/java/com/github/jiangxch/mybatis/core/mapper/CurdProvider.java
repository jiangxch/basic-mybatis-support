package com.github.jiangxch.mybatis.core.mapper;

import com.github.jiangxch.mybatis.common.util.ReflectUtil;
import com.github.jiangxch.mybatis.core.support.EntityMeta;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.Map;


/**
 * Curd模板
 *
 * @author: jiangxch
 * @date: 2020/5/10 下午10:42
 */
public class CurdProvider {
    public static final String CLASS_KEY = "clazz";
    public static final String PARAM_KEY = "param";


    public String findAll(Class<?> entityClass) {
        EntityMeta meta = EntityMeta.parse(entityClass);

        return new SQL() {
            {
                SELECT("*");
                FROM(meta.getTableNae());
            }
        }.toString();
    }

    public String findById(Map<String, Object> map) {
        Class<?> entityClazz = (Class<?>) map.get(CLASS_KEY);
        EntityMeta meta = EntityMeta.parse(entityClazz);
        return new SQL() {
            {
                SELECT("*");
                FROM(meta.getTableNae());
                WHERE("id=#{" + PARAM_KEY + "}");
            }
        }.toString();
    }


    public String countAll(Class<?> entityClass) {
        EntityMeta meta = EntityMeta.parse(entityClass);
        return new SQL() {
            {
                SELECT("count(0)");
                FROM(meta.getTableNae());
            }
        }.toString();
    }


    public String deleteAll(Class<?> entityClass) {
        EntityMeta meta = EntityMeta.parse(entityClass);
        return new SQL() {
            {
                DELETE_FROM(meta.getTableNae());
            }
        }.toString();
    }


    public String truncate(Class<?> entityClass) {
        EntityMeta meta = EntityMeta.parse(entityClass);
        return "TRUNCATE TABLE " + meta.getTableNae();
    }

    public String deleteById(Map<String,Object> map) {
        Class<?> entityClazz = (Class<?>) map.get(CLASS_KEY);
        EntityMeta meta = EntityMeta.parse(entityClazz);
        return new SQL() {
            {
                DELETE_FROM(meta.getTableNae());
                WHERE("id=#{" + PARAM_KEY + "}");
            }
        }.toString();
    }

    public String insert(Object object) {
        EntityMeta meta = EntityMeta.parse(object.getClass());
        Map<String, Field> coloums = meta.getColoums();
        StringBuilder names = new StringBuilder(),values = new StringBuilder();
        int i=0;
        for (Map.Entry<String, Field> kv : coloums.entrySet()) {
            if (null == ReflectUtil.getFieldValue(kv.getValue(),object)) {
                continue;
            }
            if (i++ != 0) {
                names.append(",");
                values.append(",");
            }
            names.append("`").append(kv.getKey()).append("`");
            values.append("#{").append(kv.getValue().getName()).append("}");
        }

        return new SQL()
                .INSERT_INTO(meta.getTableNae())
                .VALUES(names.toString(),values.toString())
                .toString();
    }
}
