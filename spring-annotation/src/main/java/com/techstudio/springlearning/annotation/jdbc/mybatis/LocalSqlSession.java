package com.techstudio.springlearning.annotation.jdbc.mybatis;

import com.github.pagehelper.PageInterceptor;
import com.techstudio.springlearning.annotation.jdbc.mybatis.dao.ArticleMapper;
import com.techstudio.springlearning.annotation.jdbc.mybatis.dao.BlogMapper;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.InputStream;

/**
 * @author lj
 * @date 2020/2/23
 */
public class LocalSqlSession {

    private static final ThreadLocal<SqlSession> SQL_SESSION_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * SqlSession 应该设计成线程内共享、线程间互斥，及在一次请求中有一个SqlSession
     * 所以ThreadLocal非常适合放SqlSession
     * <p>
     * 以下为官方描述：
     * <p>
     * 每个线程都应该有自己的SqlSession实例。SqlSession的实例不能共享，也不是线程安全的。因此，最好的范围是请求或方法范围。
     * 永远不要在静态字段甚至类的实例字段中保留对SqlSession实例的引用。永远不要在任何托管范围内保留对SqlSession的引用，
     * 例如Servlet框架的HttpSession。如果您正在使用任何类型的web框架，请考虑使用与HTTP请求范围类似的SqlSession。
     * 换句话说，在接收到HTTP请求时，可以打开SqlSession，然后在返回响应时，可以关闭它。结束会议非常重要。
     * 您应该始终确保它在finally块中关闭。下面是确保关闭SqlSessions的标准模式:
     *
     * @return SqlSession
     */
    public static SqlSession getSqlSession() {
        SqlSession session = SQL_SESSION_THREAD_LOCAL.get();
        if (session == null) {
            session = getSqlSessionFactory().openSession();
            SQL_SESSION_THREAD_LOCAL.set(session);
        }
        return session;
    }

    public static void removeSqlSession() {
        SqlSession session = SQL_SESSION_THREAD_LOCAL.get();
        if (session != null) {
            // 关闭数据库回话，如果配置了连接池则将connection归还
            session.close();
        }
        SQL_SESSION_THREAD_LOCAL.remove();
    }

    /**
     * 映射器是用来绑定到映射语句的接口。mapper接口的实例是从SqlSession获取的。
     * 因此，从技术上讲，任何mapper实例最广泛的作用域与请求它们的SqlSession相同。
     * 然而，mapper实例的最佳作用域是方法作用域。也就是说，应该在使用它们的方法中请求它们，然后丢弃它们。
     * 它们不需要显式地关闭。虽然在整个请求过程中保留它们不是问题，类似于SqlSession，
     * 但是您可能会发现在这个级别上管理太多资源会很快失控。保持简单，保持映射器在方法范围内。
     *
     * @param requireType
     * @param <T>
     * @return
     */
    public static <T> T getMapper(Class<T> requireType) {
        return getSqlSession().getMapper(requireType);
    }

    /**
     * SqlSessionFactory应该设计成单例,生命期存在与整个应用程序
     * <p>
     * 以下为官方：
     * <p>
     * 一旦创建，SqlSessionFactory应该在应用程序执行期间存在。应该很少或没有理由去处理或重新创建它。
     * 最好不要在应用程序运行时多次重建SqlSessionFactory。这样做应该被认为是一种“坏味道”。
     * 因此，SqlSessionFactory的最佳范围是应用程序范围。这可以通过多种方式实现。最简单的是使用单例模式或静态单例模式。
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        return LocalSqlSession.InnerSqlSessionFactory.SQL_SESSION_FACTORY;
    }

    public static SqlSessionFactory getSqlSessionFactoryWithXML() {
        String sourceLocation = "mybatis-config.xml";
        InputStream is = BlogQueryTest.class.getClassLoader().getResourceAsStream(sourceLocation);
        return new SqlSessionFactoryBuilder().build(is);
    }

    public static SqlSessionFactory getSqlSessionFactoryWithCodeConfig() {
        DataSource ds = getDataSource();
        TransactionFactory tf = new JdbcTransactionFactory();
        Environment env = new Environment("development", tf, ds);
        Configuration config = new Configuration(env);
        // mapper文件
        config.addMapper(BlogMapper.class);
        config.addMapper(ArticleMapper.class);
        config.addInterceptor(new InterceptorTest());
        // 插件
        config.addInterceptor(new PageInterceptor());
        config.setMapUnderscoreToCamelCase(true);
        config.setLazyLoadingEnabled(true);

        // 加载mapper xml文件
        InputStream is = BlogQueryTest.class.getClassLoader().getResourceAsStream("mapper/BlogMapper.xml");
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(is,
                config, "mapper/BlogMapper.xml", config.getSqlFragments());
        xmlMapperBuilder.parse();

        InputStream articleIn = BlogQueryTest.class.getClassLoader().getResourceAsStream("mapper/ArticleMapper.xml");
        XMLMapperBuilder articleXmlMapperBuilder = new XMLMapperBuilder(articleIn,
                config, "mapper/ArticleMapper.xml", config.getSqlFragments());
        articleXmlMapperBuilder.parse();

        return new SqlSessionFactoryBuilder().build(config);
    }

    public static DataSource getDataSource() {
        PooledDataSource ds = new PooledDataSource();
        ds.setDriver("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://10.200.50.173:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8");
        ds.setUsername("root");
        ds.setPassword("dbadmin");
        return ds;
    }

    public static class InnerSqlSessionFactory {
        private static final SqlSessionFactory SQL_SESSION_FACTORY;

        static {
            SQL_SESSION_FACTORY = getSqlSessionFactoryWithCodeConfig();
        }
    }

}
