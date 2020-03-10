package com.techstudio.springlearning.jpa.dao;

import com.techstudio.springlearning.jpa.entity.Article;
import com.techstudio.springlearning.jpa.entity.Blog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

/**
 * @author lj
 * @date 2020/3/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogRepositoryTest {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void test1() {

        // blogRepository.deleteById(1);

        Blog b = blogRepository.getBlog(1);

        Page<Blog> blogs = blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (Blog.class.equals(query.getResultType())) {
                    root.fetch("articles", JoinType.LEFT);
                }
                // 分页进行count时join即可，不需要fetch，因为count时返回的实体没有blog，所以就不存在fetch articles了
                else {
                    root.join("articles", JoinType.LEFT);
                }
                return query.getRestriction();
            }
        }, PageRequest.of(0, 3));

        Page<Article> articles = articleRepository.findAll((root, query, criteriaBuilder) -> {

            if (Article.class.equals(query.getResultType())) {
                root.fetch("blog", JoinType.LEFT);
            }
            else {
                root.join("blog", JoinType.LEFT);
            }
            return query.getRestriction();
        }, PageRequest.of(0, 2));

        Blog blog = blogRepository.findById(1).orElse(null);
        System.out.println(blog.toString());
    }

}