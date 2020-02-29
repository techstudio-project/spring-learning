package com.techstudio.springlearning.annotation.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * @author lj
 * @date 2020/2/26
 */
@Configuration
public class SpringJDBCConfiguration {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 内部持有了JdbcTemplate，进一步封装，允许在sql中使用具体的参数名来替代？
     * update Student set description = :description where id = :id
     * 通过一个map组织参数
     *
     * @param dataSource
     * @return
     */
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
