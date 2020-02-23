package com.techstudio.springlearning.annotation.jdbc.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;


/**
 * 支持的拦截接口
 *
 * @author lj
 * @date 2020/2/21
 * @see org.apache.ibatis.executor.Executor
 * @see org.apache.ibatis.executor.parameter.ParameterHandler
 * @see org.apache.ibatis.executor.resultset.ResultSetHandler
 * @see org.apache.ibatis.executor.statement.StatementHandler
 * @see org.apache.ibatis.plugin.Plugin
 */
@Intercepts(
        @Signature(type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}, method = "query"))
public class InterceptorTest implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("===mybatis interceptor chain test=== before method execute ");
        Object result = invocation.proceed();
        System.out.println("===mybatis interceptor chain test=== after method execute ");
        return result;
    }
}
