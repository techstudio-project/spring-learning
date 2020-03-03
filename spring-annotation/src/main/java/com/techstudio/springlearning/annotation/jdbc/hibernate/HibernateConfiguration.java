package com.techstudio.springlearning.annotation.jdbc.hibernate;

import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author lj
 * @date 2020/2/29
 */
@Configuration
public class HibernateConfiguration {

    @Bean
    public DataSource dataSource() {
        DruidDataSource druid = new DruidDataSource();
        druid.setUrl("jdbc:mysql://10.200.50.173:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8");
        druid.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druid.setUsername("root");
        druid.setPassword("dbadmin");
        return druid;
    }

    @Bean
    public LocalSessionFactoryBean hibernateSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        // 配置数据源
        factoryBean.setDataSource(dataSource);

        // hibernateProperties配置
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.format_sql", "true");
        hibernateProperties.setProperty("hibernate.format_sql", "true");

        // 二级缓存
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "true");
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", "ehcache-singleton");
        // 开启查询缓存，及查询后将结果缓存，需要在查询是显示指定query.setCacheable(true)，或者在实体类注解@Cacheable
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", "true");
        factoryBean.setHibernateProperties(hibernateProperties);

        // 实体类映射扫描
        factoryBean.setPackagesToScan("com.techstudio.springlearning.annotation.jdbc.entity");
        return factoryBean;
    }

    @Bean
    public HibernateTemplate hibernateTemplate(SessionFactory sessionFactory) {
        return new HibernateTemplate(sessionFactory);
    }

    /**
     * 使用LocalSessionFactoryBuilder构建SessionFactory
     *
     * @param dataSource
     * @return
     */
    public SessionFactory hibernateSessionFactoryWithBuilder(DataSource dataSource) {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource);
        return builder.buildSessionFactory();
    }


}
