package com.techstudio.springlearning.jpa.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.techstudio.springlearning.jpa.util.PropertiesUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author lj
 * @date 2020/3/4
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.techstudio.springlearning.jpa.dao")
public class JpaConfiguration {

    private static final String DATA_SOURCE_LOCATION = "datasource.properties";

    @Bean
    public DataSource dataSource() throws IOException {
        DruidDataSource druid = new DruidDataSource();
        Properties prop = PropertiesUtils.loadPropertiesCache(DATA_SOURCE_LOCATION);
        druid.setUrl(prop.getProperty("datasource.url"));
        druid.setDriverClassName(prop.getProperty("datasource.driver"));
        druid.setUsername(prop.getProperty("datasource.username"));
        druid.setPassword(prop.getProperty("datasource.password"));
        return druid;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    /**
     * 配置jpa提供方，这里配置hibernate
     *
     * @return JpaVendorAdapter
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        // 设置hibernate方言
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        // 设置不生成ddl语句
        adapter.setGenerateDdl(false);
        adapter.setShowSql(true);
        return adapter;
    }

    /**
     * 就类似于hibernate的SessionFactory,他们都继承自jpa的EntityManagerFactory
     *
     * @return LocalContainerEntityManagerFactoryBean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter adapter) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(adapter);
        // 实体类扫描
        bean.setPackagesToScan("com.techstudio.springlearning.jpa.entity");
        return bean;
    }

}
