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


}
