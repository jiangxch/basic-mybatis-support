package com.github.jiangxch.mybatis.core.plugins;

import com.github.jiangxch.mybatis.common.util.ReflectUtil;
import com.github.jiangxch.mybatis.core.annotations.AddEntityParam;
import com.github.jiangxch.mybatis.core.annotations.FillResultMap;
import com.github.jiangxch.mybatis.core.mapper.CurdProvider;
import com.github.jiangxch.mybatis.core.support.EntityMeta;
import com.github.jiangxch.mybatis.core.support.MapperMeta;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 填充curd需要的entity参数,为CURD操作提供便捷
 *
 * @author: jiangxch
 * @date: 2020/5/10 下午10:33
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class FillEntityPlugin implements Interceptor {

    private Map<String, MapperMeta> CACHE = Maps.newConcurrentMap();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        if (target instanceof Executor) {
            // 填充entity对象到参数中,为curdProvider模板提供entity参数
            fillEntityArg(invocation, (BaseExecutor) target);
        }
        return invocation.proceed();
    }

    @SuppressWarnings("unchecked")
    private void fillEntityArg(Invocation invocation, BaseExecutor target) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        MapperMeta mapperMeta = getMapperMeta(ms.getConfiguration(), ms.getId());

        if (mapperMeta.isAddEntityParam()) {
            Object param = args[1];
            if (param == null) {
                // CurdProvider方法不需要参数,将entityMeta传过去
                param = mapperMeta.getEntityClass();
            } else if (param instanceof Map) {
                // 参数本来是map类型将Class put进去
                ((Map) param).put(CurdProvider.CLASS_KEY, mapperMeta.getEntityClass());
            } else {
                // 参数不是map,将参数替换为map
                Map<String, Object> map = new HashMap<>();
                map.put(CurdProvider.PARAM_KEY, param);
                map.put(CurdProvider.CLASS_KEY, mapperMeta.getEntityClass());
                param = map;
            }
            args[1] = param;
        }

        // 当需要进行数据库列值映射到对象属性，需要填充MappedStatement的resultMaps,否则查询结果会报错
        if (mapperMeta.isFillResultMap()) {
            MetaObject metaObject = getMetaObject(ms);
            metaObject.setValue("resultMaps", mapperMeta.getResultMaps());
        }
    }

    /**
     * @param configuration
     * @param statementId com.xxx.BlogMapper.findAll
     * @return
     */
    private MapperMeta getMapperMeta(Configuration configuration, String statementId) throws ClassNotFoundException {
        int position = statementId.lastIndexOf(".");
        String mapperClassName = statementId.substring(0, position);
        String methodName = statementId.substring(position +1);
        MapperMeta mapperMeta = CACHE.get(statementId);
        if (mapperMeta == null) {
            mapperMeta = new MapperMeta();
            Class<?> mapperClass = Class.forName(mapperClassName);
            Class<?> entityClass = ReflectUtil.getGenericClass(mapperClass);
            Method buildSqlMethod = getBuildSqlMethod(mapperClass,methodName);
            if (entityClass == null) {
                throw new NullPointerException("the mapper[" + mapperClassName + "] interface must extend a super interface and statement a generic entity");
            }
            if (buildSqlMethod.isAnnotationPresent(FillResultMap.class)) {
                mapperMeta.setFillResultMap(true);
            } else {
                mapperMeta.setFillResultMap(false);
            }
            if (buildSqlMethod.isAnnotationPresent(AddEntityParam.class)) {
                mapperMeta.setAddEntityParam(true);
            } else {
                mapperMeta.setAddEntityParam(false);
            }
            mapperMeta.setEntityClass(entityClass);
            mapperMeta.setResultMaps(Lists.newArrayList(buildResultMap(configuration,mapperClassName,entityClass)));
            CACHE.put(statementId, mapperMeta);
        }
        return mapperMeta;
    }

    private Method getBuildSqlMethod(Class<?> mapperClass,String methodName) {
        Method[] methods = mapperClass.getMethods();
        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        throw new RuntimeException("can't find matched build sql method with method name=[" + methodName + "]");
    }

    private ResultMap buildResultMap(Configuration configuration,String mapperClassName, Class<?> entityClass) {
        String resultMapId = mapperClassName + "." + "GeneratedResultMap";

        if (configuration.hasResultMap(resultMapId)) {
            return configuration.getResultMap(resultMapId);
        }
        List<ResultMapping> resultMappings = getResultMappings(entityClass, configuration);
        ResultMap.Builder builder = new ResultMap.Builder(configuration,resultMapId,entityClass,resultMappings);
        ResultMap build = builder.build();

        configuration.addResultMap(build);
        return build;
    }

    private List<ResultMapping> getResultMappings(Class<?> entityClass, Configuration configuration) {
        List<ResultMapping> resultMappings = new ArrayList<>();
        Map<String, Field> columns = EntityMeta.getColumns(entityClass);
        for (Map.Entry<String, Field> entry : columns.entrySet()) {
            String tableColumnName = entry.getKey();
            Field field = entry.getValue();

            Class<?> fieldJavaType = resolveResultJavaType(entityClass,field.getName());
            List<ResultFlag> resultFlags = new ArrayList<>();
            // 字段名称是id即为逐渐,此处不用注解标识
            if ("id".equals(tableColumnName.toLowerCase())) {
                resultFlags.add(ResultFlag.ID);
            }
            resultMappings.add(buildResultMapping(configuration, field.getName(), tableColumnName, fieldJavaType, resultFlags));
        }
        return resultMappings;
    }

    private ResultMapping buildResultMapping(Configuration configuration, String fieldName, String tableColumnName, Class<?> fieldJavaType, List<ResultFlag> resultFlags) {
        ResultMapping.Builder builder = new ResultMapping.Builder(configuration, fieldName, tableColumnName, fieldJavaType);
        builder.flags(resultFlags);
        builder.composites(new ArrayList<>());
        builder.notNullColumns(new HashSet<>());
        return builder.build();
    }

    /**
     * 解析entity中指定字段名称的字段类型
     * @param entityClass
     * @param fieldName
     * @return
     */
    private Class<?> resolveResultJavaType(Class<?> entityClass, String fieldName) {
        MetaClass metaClass = MetaClass.forClass(entityClass, new DefaultReflectorFactory());
        return metaClass.getSetterType(fieldName);
    }


    private MetaObject getMetaObject(MappedStatement ms) {
        return SystemMetaObject.forObject(ms);
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }


}
