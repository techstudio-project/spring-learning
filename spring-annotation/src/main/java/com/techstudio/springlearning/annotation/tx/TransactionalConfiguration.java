package com.techstudio.springlearning.annotation.tx;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * 开启 基于注解的事务配置 annotation-driven @EnableTransactionManagement，类似于xml的tx:annotation-driven
 * 原理还是基于aop来实现方法级别的事务管理，
 * 注册了BeanFactoryTransactionAttributeSourceAdvisor和 TransactionInterceptor（advice）
 *
 * @author lj
 * @date 2020/2/25
 * @see org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor
 * @see org.springframework.transaction.interceptor.TransactionInterceptor
 */
@Configuration
@EnableTransactionManagement
public class TransactionalConfiguration implements TransactionManagementConfigurer {

    private final DataSource dataSource;

    private final SessionFactory sessionFactory;

    public TransactionalConfiguration(DataSource dataSource, SessionFactory sessionFactory) {
        this.dataSource = dataSource;
        this.sessionFactory = sessionFactory;
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager ptm) {
        return new TransactionTemplate(ptm);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new HibernateTransactionManager(sessionFactory);
    }
}
