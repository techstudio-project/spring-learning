package com.techstudio.springlearning.annotation.jdbc.mybatis;

import com.techstudio.springlearning.annotation.jdbc.mybatis.entity.Blog;

import java.util.List;

/**
 * @author lj
 * @date 2020/2/24
 */
public interface BlogDao {

    List<Blog> findAll();

}
