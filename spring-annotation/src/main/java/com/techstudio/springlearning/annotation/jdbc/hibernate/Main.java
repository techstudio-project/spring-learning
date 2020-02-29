package com.techstudio.springlearning.annotation.jdbc.hibernate;

import com.alibaba.fastjson.JSON;
import com.techstudio.springlearning.annotation.jdbc.entity.Blog;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.techstudio.springlearning.annotation.jdbc.hibernate.HibernateLocalSessionFactory.getCurrentSession;

/**
 * @author lj
 * @date 2020/2/26
 */
public class Main {

    public static void main(String[] args) {
        Session session = null;
        try {
            session = getCurrentSession();

            // 事务必须处于开启状态(应该是hibernate强制使用事务)
            session.getTransaction().begin();
            criteriaTest(session);
            session.getTransaction().commit();
        }
        catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        }
    }

    public static void findTest(Session session) {
        Blog blog = session.find(Blog.class, 1);
        System.out.println(JSON.toJSONString(blog));
    }

    public static void criteriaTest(Session session) {

        // session.createCriteria(Blog.class).list();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Blog> criteria = cb.createQuery(Blog.class);
        Root<Blog> root = criteria.from(Blog.class);
        root.fetch("articles", JoinType.LEFT);
        List<Blog> blogs = session.createQuery(criteria).getResultList();
        System.out.println(blogs);
    }


}
