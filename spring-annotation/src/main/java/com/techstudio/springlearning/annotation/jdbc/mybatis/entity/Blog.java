package com.techstudio.springlearning.annotation.jdbc.mybatis.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 开启mybatis二级缓存的情况下需要实现序列化接口
 *
 * @author lj
 * @date 2020/2/22
 */
public class Blog implements Serializable {

    private Integer id;

    private String name;

    private Article article;

    private List<Article> articles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
