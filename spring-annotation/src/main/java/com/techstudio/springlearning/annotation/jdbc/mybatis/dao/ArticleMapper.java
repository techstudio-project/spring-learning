package com.techstudio.springlearning.annotation.jdbc.mybatis.dao;

import com.techstudio.springlearning.annotation.jdbc.entity.Article;

import java.util.List;
import java.util.Map;

/**
 * @author lj
 * @date 2020/2/19
 */
public interface ArticleMapper {

    List<Article> findAll();

    List<Article> findWithCondition(Map<String, Object> condition);

    void update(Article article);

}
