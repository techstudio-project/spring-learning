package com.techstudio.springlearning.annotation.jdbc.mybatis;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.techstudio.springlearning.annotation.jdbc.mybatis.dao.ArticleMapper;
import com.techstudio.springlearning.annotation.jdbc.mybatis.dao.BlogMapper;
import com.techstudio.springlearning.annotation.jdbc.entity.Article;
import com.techstudio.springlearning.annotation.jdbc.entity.Blog;

import java.util.List;

import static com.techstudio.springlearning.annotation.jdbc.mybatis.LocalSqlSession.getMapper;
import static com.techstudio.springlearning.annotation.jdbc.mybatis.LocalSqlSession.getSqlSession;

/**
 * @author lj
 * @date 2020/2/19
 */
public class BlogQueryTest {

    public static void main(String[] args) {

        //queryArticleTest();

        queryBlogTest();

        // 出去时将sqlSession从threadLocal中删除
        LocalSqlSession.removeSqlSession();
    }

    /**
     * 1.2（mapper接口方式） mapper.xml抽象对应的mapper接口，通过mapper接口提供数据库交互，
     * 这种方式更加迎合面向接口的编程方式
     * <p>
     * 通过动态代理技术，实际上最终还是通过1.1的方式来执行
     */
    private static void queryBlogListTest() {
        BlogMapper mapper = getMapper(BlogMapper.class);
        List<Blog> blogs = mapper.findAllNew();
        System.out.println(JSON.toJSONString(blogs));
    }

    private static void queryBlogTest() {
        BlogMapper mapper = getMapper(BlogMapper.class);
        Blog query = new Blog();
        query.setId(1);
        Blog blog = mapper.paramTest(query,"123");
        System.out.println(JSON.toJSONString(blog));
    }

    private static void queryArticleTest() {
        ArticleMapper mapper = getMapper(ArticleMapper.class);
        List<Article> articles = mapper.findAll();
        System.out.println(JSON.toJSONString(articles));
    }

    /**
     * 1.1（原始方式）使用传统的mybatis提供的api进行数据库操作，通过SqlSession对象与数据库交互
     * <p>
     * 指定 Statement ID进行查询
     */
    private static void queryWithMybatisAPI() {

        // SqlSession中执行了任何一个update操作(update()、delete()、insert())
        // 都会清空PerpetualCache对象的数据，但是该对象可以继续使用
        String sqlStatement = "com.techstudio.springlearning.annotation.jdbc.mybatis.dao.BlogMapper.findAll";
        PageHelper.startPage(2, 3);
        List<Blog> list = getSqlSession().selectList(sqlStatement);

        Page<Blog> page = (Page<Blog>) list;
        System.out.println(JSON.toJSONString(page.toPageInfo()));

        // System.out.println(JSON.toJSONString(list));
        // 以下会命中缓存（同一个SqlSession）
        List<Blog> cachedList = getSqlSession().selectList(sqlStatement);

        // 清除一级缓存
        // session.clearCache();

        // 清除二级缓存
        // session.getConfiguration().getMappedStatement("xxx").getCache().clear();
    }

}
