package com.techstudio.springlearning.annotation.jdbc.hibernate;

import com.alibaba.fastjson.JSON;
import com.techstudio.springlearning.annotation.jdbc.entity.Blog;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author lj
 * @date 2020/2/27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CommonDaoTest {

    // private BlogService blogService = new BlogServiceImpl();

    @Autowired
    private BlogService blogService;


    @Before
    public void startTransaction() {
        // blogService.getCurrentSession().beginTransaction();
    }

    @After
    public void commitTransaction() {
        // blogService.getCurrentSession().getTransaction().commit();
    }

    @Test
    public void save() {
        Blog blog = new Blog();
        blog.setName("12312321332");
        Integer id = (Integer) blogService.save(blog);
    }

    @Test
    public void batchSave() {
    }

    @Test
    public void saveOrUpdate() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void deleteById() {
    }

    @Test
    public void batchDelete() {
    }

    @Test
    public void get() {
        Blog blog = blogService.get(Blog.class, 1);
        System.out.println(JSON.toJSONString(blog.getName()));
    }

    @Test
    public void findUniqueByProperty() {
    }

    @Test
    public void testFindUniqueByProperty() {
    }

    @Test
    public void findByProperty() {
    }

    @Test
    public void testFindByProperty() {
    }

    @Test
    public void loadAll() {
        List<Blog> blogs = blogService.loadAll(Blog.class);
        System.out.println(JSON.toJSONString(blogs));
    }

    /**
     * 这里将演示join的方式进行迫切查询
     */
    @Test
    public void findByCriteriaQuery() {
        List<Blog> blogs = blogService.findByCriteriaQuery(blogCriteriaJoinQuery());
        System.out.println(JSON.toJSONString(blogs));
    }

    private CriteriaQuery<Blog> blogCriteriaJoinQuery() {
        Session session = blogService.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Blog> criteriaQuery = builder.createQuery(Blog.class);
        Root<Blog> root = criteriaQuery.from(Blog.class);
        root.fetch("articles", JoinType.LEFT);
        return criteriaQuery;
    }
}