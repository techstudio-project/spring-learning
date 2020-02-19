package com.techstudio.springlearning.annotation.jdbc.mybatis;

import com.alibaba.fastjson.JSON;
import com.techstudio.springlearning.annotation.jdbc.mybatis.dao.AssessmentMapper;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author lj
 * @date 2020/2/19
 */
public class MybatisTest {


    public static void main(String[] args) {
        queryWithMybatisAPI();
    }

    /**
     * 1.2（mapper接口方式） mapper.xml抽象对应的mapper接口，通过mapper接口提供数据库交互，
     * 这种方式更加迎合面向接口的编程方式
     * <p>
     * 通过动态代理技术，实际上最终还是通过1.1的方式来执行
     */
    private static void queryWithMapper() {
        SqlSessionFactory ssf = getSqlSessionFactory();
        try (SqlSession session = ssf.openSession()) {
            AssessmentMapper mapper = session.getMapper(AssessmentMapper.class);
            List<Map<String, Object>> list = mapper.findAll();
            System.out.println(JSON.toJSONString(list));
        }
    }

    /**
     * 1.1（原始方式）使用传统的mybatis提供的api进行数据库操作，通过SqlSession对象与数据库交互
     * <p>
     * 指定 Statement ID进行查询
     */
    private static void queryWithMybatisAPI() {
        SqlSessionFactory ssf = getSqlSessionFactoryWithXML();
        try (SqlSession session = ssf.openSession()) {
            List<Map<String, Object>> list = session.selectList("com.techstudio.springlearning.annotation.jdbc.mybatis.dao.AssessmentMapper.findAll");
            System.out.println(JSON.toJSONString(list));
        }
    }

    private static SqlSessionFactory getSqlSessionFactoryWithXML() {
        String sourceLocation = "mybatis-config.xml";
        InputStream is = MybatisTest.class.getClassLoader().getResourceAsStream(sourceLocation);
        return new SqlSessionFactoryBuilder().build(is);
    }

    private static SqlSessionFactory getSqlSessionFactoryWithCodeConfig() {
        PooledDataSource pds = new PooledDataSource();
        pds.setDriver("com.mysql.cj.jdbc.Driver");
        pds.setUrl("jdbc:mysql://10.1.248.147:3306/am_general_qhln?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8");
        pds.setUsername("am");
        pds.setPassword("amdbadmin,");

        TransactionFactory tf = new JdbcTransactionFactory();
        Environment env = new Environment("development", tf, pds);
        Configuration config = new Configuration(env);
        config.addMapper(AssessmentMapper.class);

        // 加载mapper文件
        InputStream is = MybatisTest.class.getClassLoader().getResourceAsStream("mapper/AssessmentMapper.xml");
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(is,
                config, "mapper/AssessmentMapper.xml", config.getSqlFragments());
        xmlMapperBuilder.parse();

        return new SqlSessionFactoryBuilder().build(config);
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
     * SqlSession 应该设计成线程内共享、线程间互斥，及在一次请求中有一个SqlSession
     * 所以ThreadLocal非常适合放SqlSession
     * <p>
     * 一下为官方描述：
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
        return getSqlSessionFactory().openSession();
    }

    /**
     * SqlSessionFactory应该设计成单例,生命期存在与整个应用程序
     * <p>
     * 一下为官方：
     * <p>
     * 一旦创建，SqlSessionFactory应该在应用程序执行期间存在。应该很少或没有理由去处理或重新创建它。
     * 最好不要在应用程序运行时多次重建SqlSessionFactory。这样做应该被认为是一种“坏味道”。
     * 因此，SqlSessionFactory的最佳范围是应用程序范围。这可以通过多种方式实现。最简单的是使用单例模式或静态单例模式。
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        return InnerSqlSessionFactory.SQL_SESSION_FACTORY;
    }


    private static class InnerSqlSessionFactory {
        private static final SqlSessionFactory SQL_SESSION_FACTORY;

        static {
            SQL_SESSION_FACTORY = getSqlSessionFactoryWithCodeConfig();
        }
    }

}
