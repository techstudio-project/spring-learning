package com.techstudio.springlearning.annotation.jdbc.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author lj
 * @date 2020/2/29
 */
@Service
@Transactional
public class CommonServiceImpl<T> implements CommonService<T> {

    private CommonDao commonDao;

    @Autowired
    public void setCommonDao(CommonDao commonDao) {
        this.commonDao = commonDao;
    }

    @Override
    public SessionFactory getSessionFactory() {
        return commonDao.getSessionFactory();
    }

    @Override
    public Session getCurrentSession() {
        return commonDao.getCurrentSession();
    }

    @Override
    public Serializable save(T entity) {
        return commonDao.save(entity);
    }

    @Override
    public void batchSave(List<T> entities) {
        commonDao.batchSave(entities);
    }

    @Override
    public void saveOrUpdate(T entity) {
        commonDao.saveOrUpdate(entity);
    }

    @Override
    public void update(T entity) {
        commonDao.update(entity);
    }

    @Override
    public void delete(T entity) {
        commonDao.delete(entity);
    }

    @Override
    public void deleteById(Class<T> entityClass, Serializable id) {
        commonDao.deleteById(entityClass, id);
    }

    @Override
    public void batchDelete(Collection<T> entities) {
        commonDao.batchDelete(entities);
    }

    @Override
    public T get(Class<T> entityClass, Serializable id) {
        return commonDao.get(entityClass, id);
    }

    @Override
    public T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
        return commonDao.findUniqueByProperty(entityClass, propertyName, value);
    }

    @Override
    public T findUniqueByProperty(Class<T> entityClass, Map<String, Object> properties) {
        return commonDao.findUniqueByProperty(entityClass, properties);
    }

    @Override
    public List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
        return commonDao.findByProperty(entityClass, propertyName, value);
    }

    @Override
    public List<T> findByProperty(Class<T> entityClass, Map<String, Object> properties) {
        return commonDao.findByProperty(entityClass, properties);
    }

    @Override
    public List<T> loadAll(Class<T> entityClass) {
        return commonDao.loadAll(entityClass);
    }

    @Override
    public List<T> findByCriteriaQuery(CriteriaQuery<T> cq) {
        return commonDao.findByCriteriaQuery(cq);
    }
}
