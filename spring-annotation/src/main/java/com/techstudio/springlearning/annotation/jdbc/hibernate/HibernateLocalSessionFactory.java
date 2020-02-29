package com.techstudio.springlearning.annotation.jdbc.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author lj
 * @date 2020/2/26
 */
public class HibernateLocalSessionFactory {

    public static Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }

    public static SessionFactory getSessionFactory() {
        return InnerSessionFactory.SESSION_FACTORY;
    }

    private static class InnerSessionFactory {
        private static final SessionFactory SESSION_FACTORY;

        static {
            // 1.初始化配置类
            Configuration cfg = new Configuration();
            // 2.加载配置文件
            cfg.configure("hibernate.cfg.xml");
            // 3.创建 SESSION_FACTORY
            SESSION_FACTORY = cfg.buildSessionFactory();
        }
    }

}
