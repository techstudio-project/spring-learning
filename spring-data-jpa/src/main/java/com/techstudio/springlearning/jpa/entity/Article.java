package com.techstudio.springlearning.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author lj
 * @date 2020/2/22
 */
@Entity
@Table(name = "t_article")
public class Article implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "blog_id")
    private Integer blogId;


    @JoinColumn(name = "blog_id", insertable = false, updatable = false)
    @ManyToOne()
    private Blog blog;

    @Column(name = "content")
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
}
