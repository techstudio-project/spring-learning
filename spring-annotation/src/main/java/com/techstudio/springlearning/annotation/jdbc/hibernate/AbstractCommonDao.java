package com.techstudio.springlearning.annotation.jdbc.hibernate;

import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.*;

/**
 * @author lj
 * @date 2020/2/27
 */
public abstract class AbstractCommonDao implements CommonDao {

    @Override
    public Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }

    @Override
    public <T> Serializable save(T entity) {
        Serializable id = getCurrentSession().save(entity);
        System.out.println(id);
        return id;
    }

    @Override
    public <T> void batchSave(List<T> entities) {
        for (int i = 0; i < entities.size(); i++) {
            getCurrentSession().save(entities.get(i));
            if (i % 1000 == 0) {
                getCurrentSession().flush();
                getCurrentSession().clear();
            }
        }
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    @Override
    public <T> void saveOrUpdate(T entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public <T> void update(T entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public <T> void delete(T entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public <T> void deleteById(Class<T> entityClass, Serializable id) {
        delete(get(entityClass, id));
    }

    @Override
    public <T> void batchDelete(Collection<T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public <T> T get(Class<T> entityClass, Serializable id) {
        return getCurrentSession().get(entityClass, id);
    }

    @Override
    public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(propertyName, value);
        return findUniqueByProperty(entityClass, properties);
    }

    @Override
    public <T> T findUniqueByProperty(Class<T> entityClass, Map<String, Object> properties) {
        CriteriaQuery<T> criteria = getDefaultCriteriaEqualQuery(entityClass, properties);
        return getCurrentSession().createQuery(criteria).uniqueResult();
    }

    @Override
    public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(propertyName, value);
        return findByProperty(entityClass, properties);
    }

    @Override
    public <T> List<T> findByProperty(Class<T> entityClass, Map<String, Object> properties) {
        CriteriaQuery<T> criteria = getDefaultCriteriaEqualQuery(entityClass, properties);
        return getCurrentSession().createQuery(criteria).getResultList();
    }

    @Override
    public <T> List<T> loadAll(Class<T> entityClass) {
        CriteriaQuery<T> criteria = getDefaultCriteriaEqualQuery(entityClass, null);
        return getCurrentSession().createQuery(criteria).getResultList();
    }

    @Override
    public <T> List<T> findByCriteriaQuery(CriteriaQuery<T> cq) {
        return getCurrentSession().createQuery(cq).getResultList();
    }

    public <T> CriteriaQuery<T> getDefaultCriteriaEqualQuery(Class<T> entityClass, Map<String, Object> properties) {
        // 获取CriteriaBuilder对象用于构建CriteriaQuery对象
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        // 构建CriteriaQuery对象，这就是最终要返回的标准查询对象
        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        Root<T> root = criteria.from(entityClass);
        if (properties == null) {
            return criteria;
        }
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> prop : properties.entrySet()) {
            predicates.add(builder.equal(root.get(prop.getKey()), prop.getValue()));
        }
        criteria.where(builder.and(predicates.toArray(new Predicate[0])));
        return criteria;
    }
}
