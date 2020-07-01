package com.github.jiangxch.mybatis.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author: jiangxch
 * @date: 2020/5/12 下午10:52
 */
public class ReflectUtil {

    /**
     * 获取mapper接口上的泛型
     * @param mapperClass
     * @return
     */
    public static Class<?> getGenericClass(Class<?> mapperClass) {
        Type[] genericInterfaces = mapperClass.getGenericInterfaces();
        if (genericInterfaces == null || genericInterfaces.length == 0) {
            return null;
        }
        Type iCurdMapperType = genericInterfaces[0];
        if (!(iCurdMapperType instanceof ParameterizedType)) {
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) iCurdMapperType).getActualTypeArguments();
        // 泛型类
        Class<?> entityClazz = (Class<?>) actualTypeArguments[0];
        return entityClazz;
    }

    /**
     * 获取obj对象某个字段的值
     * @param field 字段
     * @param obj 对象
     * @return obj对象中field字段的值
     */
    public static Object getFieldValue(Field field,Object obj) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {

        } finally {
            field.setAccessible(accessible);
        }

        return result;
    }
}
