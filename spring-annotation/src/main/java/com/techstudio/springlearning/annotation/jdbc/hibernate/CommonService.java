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
 * @date 2020/2/29
 */
public interface CommonService<T> {

    SessionFactory getSessionFactory();

    Session getCurrentSession();

    Serializable save(T entity);

    void batchSave(List<T> entities);

    void saveOrUpdate(T entity);

    void update(T entity);

    void delete(T entity);

    void deleteById(Class<T> entityClass, Serializable id);

    void batchDelete(Collection<T> entities);

    T get(Class<T> entityClass, Serializable id);

    T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value);

    T findUniqueByProperty(Class<T> entityClass, Map<String, Object> properties);

    List<T> findByProperty(Class<T> entityClass, String propertyName, Object value);

    List<T> findByProperty(Class<T> entityClass, Map<String, Object> properties);

    List<T> loadAll(Class<T> entityClass);

    List<T> findByCriteriaQuery(CriteriaQuery<T> cq);

}
