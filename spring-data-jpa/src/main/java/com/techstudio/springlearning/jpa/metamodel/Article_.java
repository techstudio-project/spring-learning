package com.techstudio.springlearning.jpa.metamodel;

import com.techstudio.springlearning.jpa.entity.Article;
import com.techstudio.springlearning.jpa.entity.Blog;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author lj
 * @date 2020/3/9
 */
@StaticMetamodel(Article.class)
public class Article_ {
    public static volatile SingularAttribute<Article, Integer> id;
    public static volatile SingularAttribute<Article, Integer> blogId;
    public static volatile SingularAttribute<Article, Blog> blog;
    public static volatile SingularAttribute<Article, String> content;
}
