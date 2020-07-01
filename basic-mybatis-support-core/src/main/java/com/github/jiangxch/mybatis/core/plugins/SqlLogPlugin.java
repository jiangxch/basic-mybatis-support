package com.github.jiangxch.mybatis.core.plugins;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 * 记录 mybatis 执行的SQL语句
 * @author: jiangxch
 * @date: 2020/5/8 上午12:16
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
@Slf4j
public class SqlLogPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        if (target instanceof StatementHandler) {
            StatementHandler state = (StatementHandler) target;
            log.info(state.getBoundSql().getSql());
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // target就是四大对象Executor,StatementHandler,Paramehandler,ResultSetHandler
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target,this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        Object test = properties.get("test");
    }
}
