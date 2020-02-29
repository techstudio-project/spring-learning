package com.techstudio.springlearning.annotation.jdbc.mybatis.spring;

import com.github.pagehelper.PageInterceptor;
import com.techstudio.springlearning.annotation.jdbc.mybatis.InterceptorTest;
import com.techstudio.springlearning.annotation.jdbc.mybatis.LocalSqlSession;
import com.techstudio.springlearning.annotation.util.SpringResourceUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * 配置mapper接口扫描也有很多方式
 * MapperScannerConfigurer 一般xml配置比较合适,直接声明一个bean即可
 * 使用@MapperScan 注解 （个人比较喜欢的方式）
 * 使用 <mybatis:scan/>
 *
 * @author lj
 * @date 2020/2/23
 * @see org.mybatis.spring.mapper.MapperScannerConfigurer
 */
//@Configuration
//@MapperScan("com.techstudio.springlearning.annotation.jdbc.mybatis.dao")
public class Config {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public DataSource dataSource() {
        return LocalSqlSession.getDataSource();
    }

    @Bean
    public org.apache.ibatis.session.Configuration mybatisConfiguration() {
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        config.addInterceptor(new InterceptorTest());
        PageInterceptor pi = new PageInterceptor();
        Properties properties = new Properties();
        // 分页插件会自动检测当前的数据库链接，自动选择合适的分页方式
        // 这里只是做了一个配置的例子
        properties.setProperty("helperDialect", "mysql");
        pi.setProperties(properties);
        config.addInterceptor(new PageInterceptor());
        config.setMapUnderscoreToCamelCase(true);
        config.setLazyLoadingEnabled(true);
        return config;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource,
                                                   org.apache.ibatis.session.Configuration config) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfiguration(config);
        try {
            factoryBean.setMapperLocations(SpringResourceUtils.getResources("classpath:mapper/*.xml"));
        }
        catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
        return factoryBean.getObject();
    }

    /**
     * 这是线程安全的实现类，可以在其它dao中共享使用
     * 当调用 SQL 方法时（包括由 getMapper() 方法返回的映射器中的方法），SqlSessionTemplate 将会保证使用的 SqlSession
     * 与当前 Spring 的事务相关。此外，它管理 session 的生命周期，包含必要的关闭、提交或回滚操作。
     * 另外，它也负责将 MyBatis 的异常翻译成 Spring 中的 DataAccessExceptions
     *
     * @param factory
     * @return
     */
    //@Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory);
    }


}
