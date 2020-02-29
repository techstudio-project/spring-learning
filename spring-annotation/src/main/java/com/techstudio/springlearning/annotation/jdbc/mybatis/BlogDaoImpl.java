package com.techstudio.springlearning.annotation.jdbc.mybatis;

import com.techstudio.springlearning.annotation.jdbc.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 自定义实现dao，对于需要改变mybatis默认实现行为，这是一种解决方案
 *
 * @author lj
 * @date 2020/2/24
 */
//@Repository
public class BlogDaoImpl extends SqlSessionDaoSupport implements BlogDao {

    public BlogDaoImpl(SqlSessionTemplate template) {
        setSqlSessionTemplate(template);
    }

    @Override
    public List<Blog> findAll() {
        SqlSession session = getSqlSession();
        return session.selectList("com.techstudio.springlearning.annotation.jdbc.mybatis.dao.BlogMapper.findAll");
    }
}
