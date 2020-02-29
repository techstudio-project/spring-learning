package com.techstudio.springlearning.annotation.jdbc.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 开启mybatis二级缓存的情况下需要实现序列化接口
 *
 * @author lj
 * @date 2020/2/22
 */
@Entity
@Table(name = "t_blog")
public class Blog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Transient
    private Article article;

    /**
     * 实体类为了更好的通用性，不过多的限制关联查询的方式，即默认lazy的方式，
     * 如果需要迫切查询可通过标准查询设置是否需要join
     * 这样的好处是在单表查询的业务中不会去进行不必要的关联查询
     * 对于明确只是单表查询的业务中应该避免去调用lazy查询的字段，否则lazy将失去意义
     */
    @OneToMany
    @JoinColumn(name = "blog_id")
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
