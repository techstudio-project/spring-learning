package com.techstudio.springlearning.annotation.jdbc.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author lj
 * @date 2020/2/27
 */
public interface CommonDao {

    SessionFactory getSessionFactory();

    Session getCurrentSession();

    <T> Serializable save(T entity);

    <T> void batchSave(List<T> entities);

    <T> void saveOrUpdate(T entity);

    <T> void update(T entity);

    <T> void delete(T entity);

    <T> void deleteById(Class<T> entityClass, Serializable id);

    <T> void batchDelete(Collection<T> entities);

    <T> T get(Class<T> entityClass, Serializable id);

    <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value);

    <T> T findUniqueByProperty(Class<T> entityClass, Map<String, Object> properties);

    <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value);

    <T> List<T> findByProperty(Class<T> entityClass, Map<String, Object> properties);

    <T> List<T> loadAll(Class<T> entityClass);

    <T> List<T> findByCriteriaQuery(CriteriaQuery<T> cq);

}
