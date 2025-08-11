package com.sheepion.demo.interceptor;

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

import cn.hutool.core.util.IdUtil;

/**
 * Auto generate uuid for the entity that extends the BaseEntity class.
 */
@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class AutoUuidInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];

        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        if (sqlCommandType == SqlCommandType.INSERT && parameter instanceof BaseEntity baseEntity) {
            // Generate UUID using Hutool
            baseEntity.setUuid(IdUtil.fastUUID());
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