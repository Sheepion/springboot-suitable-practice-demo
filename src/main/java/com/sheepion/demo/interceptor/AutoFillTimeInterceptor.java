package com.sheepion.demo.interceptor;

import java.time.LocalDateTime;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import com.sheepion.demo.common.BaseEntity;

/**
 * Mybatis interceptor to auto fill the createdAt and updatedAt fields of the
 * BaseEntity when insert or update.
 * <p>
 * Notably, it only works for the entities that extend the BaseEntity class. And
 * explicit fields declare is required when writing the SQL.
 * 
 */
@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class AutoFillTimeInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];

        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        if (parameter instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) parameter;
            LocalDateTime now = LocalDateTime.now();
            if (sqlCommandType == SqlCommandType.INSERT) {
                baseEntity.setCreatedAt(now);
                baseEntity.setUpdatedAt(now);
            } else if (sqlCommandType == SqlCommandType.UPDATE) {
                baseEntity.setUpdatedAt(now);
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
