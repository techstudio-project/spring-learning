package com.techstudio.springlearning.annotation.tx;

import com.alibaba.fastjson.JSON;
import com.techstudio.springlearning.annotation.jdbc.mybatis.entity.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author lj
 * @date 2020/2/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void transactionalTemplateTest() {
        accountService.transactionalTemplateTest();
    }


    @Test
    public void transactionalAop() {
        accountService.transactionalAop();
    }

    @Test
    public void jdbcTest() {
        String sql = "select * from t_article";
        List<Article> articles = jdbcTemplate.query(sql, new RowMapper<Article>() {

            @Override
            public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
                Article article = new Article();
                article.setId(rs.getInt("id"));
                article.setContent(rs.getString("content"));
                return article;
            }
        });
        System.out.println(JSON.toJSONString(articles));
    }
}