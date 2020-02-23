package com.techstudio.springlearning.annotation.jdbc.mybatis.entity;

import java.io.Serializable;

/**
 * @author lj
 * @date 2020/2/22
 */
public class Article implements Serializable {

    private Integer id;

    private Integer blogId;

    private Blog blog;

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
