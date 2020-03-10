package com.techstudio.springlearning.jpa.service.impl;

import com.techstudio.springlearning.jpa.dao.BlogRepository;
import com.techstudio.springlearning.jpa.entity.Blog;
import com.techstudio.springlearning.jpa.service.BlogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @date 2020/3/5
 */
@Service
@Transactional
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public Blog getOne(Integer id) {
        return blogRepository.getOne(id);
    }
}
