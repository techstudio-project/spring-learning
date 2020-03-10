package com.techstudio.springlearning.jpa.service;

import com.techstudio.springlearning.jpa.entity.Blog;

/**
 * @author lj
 * @date 2020/3/5
 */
public interface BlogService {

    Blog getOne(Integer id);

}
