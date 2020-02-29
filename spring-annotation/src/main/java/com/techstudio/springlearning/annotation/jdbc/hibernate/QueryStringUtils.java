package com.techstudio.springlearning.annotation.jdbc.hibernate;

import org.hibernate.Session;

import java.util.List;

/**
 * @author lj
 * @date 2020/2/27
 */
public class QueryStringUtils {

    private QueryStringUtils() {
    }

    public static Session getSession() {
        return HibernateLocalSessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findByHql(String hql) {
        return getSession().createQuery(hql).getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> T findUniqueByHql(String hql) {
        return (T) getSession().createQuery(hql).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findBySql(String sql) {
        return getSession().createSQLQuery(sql).getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> T findUniqueBySql(String sql) {
        return (T) getSession().createSQLQuery(sql).uniqueResult();
    }

}
