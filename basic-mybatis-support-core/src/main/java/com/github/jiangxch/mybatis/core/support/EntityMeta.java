package com.github.jiangxch.mybatis.core.support;

import com.github.jiangxch.mybatis.common.util.StringUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对mapper中泛型entity的描述
 *
 * @author: jiangxch
 * @date: 2020/5/12 下午10:32
 */
@Data
public class EntityMeta {

    /** Map[com.jiangxch.Blog, EntityMeta] */
    private static Map<Class<?>, EntityMeta> entityClassEntityMetaMap = new ConcurrentHashMap<>();

    /** mapper中的泛型类 */
    private Class<?> clazz;

    /** 表名 */
    private String tableNae;

    /** 字段 Map[字段对应表列名下划线命名,entity类的字段名驼峰命名(便于mybatis插入entity使用)] */
    private Map<String,Field> coloums;

    public static EntityMeta parse(Class<?> entityClazz) {
        EntityMeta entityMeta = entityClassEntityMetaMap.get(entityClazz);
        if (entityMeta == null) {
            entityMeta = new EntityMeta();
            entityMeta.setClazz(entityClazz);
            entityMeta.setTableNae(getTableName(entityClazz));
            entityMeta.setColoums(parseColumns(entityClazz));
            entityClassEntityMetaMap.put(entityClazz, entityMeta);
        }

        return entityMeta;
    }

    private static Map<String, Field> parseColumns(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<String,Field> map = new HashMap<>(fields.length);

        for (Field field : fields) {
            // 字段名称,驼峰命名
            String fieldName = field.getName();
            // 表列名,下划线命名
            String tableColumnName = StringUtil.upperCamelCase2UnderScoreCase(fieldName);
            map.put(tableColumnName, field);
        }
        return map;
    }

    private static String getTableName(Class<?> entityClazz) {
        // 默认类名转下划线即为表名
        String simpleName = entityClazz.getSimpleName();
        return StringUtil.upperCamelCase2UnderScoreCase(simpleName);
    }

    public static Map<String,Field> getColumns(Class<?> entityClass) {
        EntityMeta meta = parse(entityClass);
        return meta.getColoums();
    }
}
