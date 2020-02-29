package com.techstudio.springlearning.annotation.tx;

import com.alibaba.fastjson.JSON;
import com.techstudio.springlearning.annotation.jdbc.entity.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

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
        List<Article> articles = jdbcTemplate.query(sql, articleRowMapper());
        System.out.println(JSON.toJSONString(articles));
    }

    private RowMapper<Article> articleRowMapper() {
        return new RowMapper<Article>() {

            @Override
            public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
                Article article = new Article();
                article.setId(rs.getInt("id"));
                article.setContent(rs.getString("content"));
                return article;
            }
        };
    }

    @Test
    public void namedJdbcTest() {
        String sql = "select * from t_article where id=:id";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", 2);
        List<Article> articles = namedJdbcTemplate.query(sql, paramMap, articleRowMapper());
        System.out.println(JSON.toJSONString(articles));
    }
}