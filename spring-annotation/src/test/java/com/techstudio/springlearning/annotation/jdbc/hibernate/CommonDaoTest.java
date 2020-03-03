package com.techstudio.springlearning.annotation.jdbc.hibernate;

import com.alibaba.fastjson.JSON;
import com.techstudio.springlearning.annotation.jdbc.entity.Article;
import com.techstudio.springlearning.annotation.jdbc.entity.Blog;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

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

    @Autowired
    private TransactionTemplate transactionTemplate;


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
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Session session = blogService.getCurrentSession();
                Blog blog = session.get(Blog.class, 1);
                System.out.println(JSON.toJSONString(blog.getName()));
                // 命中一级缓存（hibernate一级缓存是基于session的）
                Blog blog1 = session.get(Blog.class, 1);
            }
        });
    }

    @Test
    public void getWithCache() {
        Blog blog = blogService.get(Blog.class, 1);
        // session 已经关闭，无法获取lazy load的结果
        // 方案1：使用OpenSessionInViewFilter或OpenSessionInViewInterceptor，但是会带来性能问题，即每个请求均会开启session直至请求完成，存在浪费
        // 方案2：既然是需要拿到lazy load的结果，为什么不提到service 的@Transactional方法中呢。
        List<Article> articles = blog.getArticles();
        Blog blog1 = blogService.get(Blog.class, 1);
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