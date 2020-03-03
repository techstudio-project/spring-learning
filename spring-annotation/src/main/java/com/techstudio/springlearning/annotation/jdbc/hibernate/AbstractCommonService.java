package com.techstudio.springlearning.annotation.jdbc.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author lj
 * @date 2020/3/2
 */
public abstract class AbstractCommonService<T> implements CommonService<T> {

    public abstract CommonDao getCommonDao();

    @Override
    public SessionFactory getSessionFactory() {
        return getCommonDao().getSessionFactory();
    }

    @Override
    public Session getCurrentSession() {
        return getCommonDao().getCurrentSession();
    }
}
