package com.techstudio.springlearning.annotation.jdbc.hibernate;

import com.techstudio.springlearning.annotation.jdbc.entity.Blog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @date 2020/2/27
 */
@Service
@Transactional
public class BlogServiceImpl extends CommonServiceImpl<Blog> implements BlogService {


    @Override
    public void getWithCache() {
        Blog blog = get(Blog.class, 1);
        // 命中缓存（在同一个session中）
        Blog blog1 = get(Blog.class, 1);
    }
}
