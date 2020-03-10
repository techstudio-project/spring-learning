package com.techstudio.springlearning.jpa.service;

import com.techstudio.springlearning.jpa.entity.Blog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author lj
 * @date 2020/3/5
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogServiceTest {

    @Autowired
    private BlogService blogService;

    @Test
    public void getOne() {
        Blog blog = blogService.getOne(1);
        System.out.println(blog.toString());
    }
}