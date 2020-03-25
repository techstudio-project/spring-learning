package com.techstudio.springlearning.annotation.jdbc.mybatis.dao;

import com.techstudio.springlearning.annotation.jdbc.entity.Article;
import com.techstudio.springlearning.annotation.jdbc.entity.Blog;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lj
 * @date 2020/2/19
 */
public interface BlogMapper {

    /**
     * java8之前编译会将方法参数名称修改为arg0 arg1这样的形式，所以造成mybatis无法绑定sql参数
     * java8之后不需要加 @param java8  但是需要加编译参数 -parameters
     *
     * @return
     */
    List<Blog> findAll();

    List<Blog> findAllNew();

    Blog findById(Integer id);


    Article findOneArticle(Integer blogId);

    List<Article> findArticles(Integer blogId);

    @Select("select * from t_blog")
    List<Blog> findAll1();

    Object insert();

    Object update();

    Object delete();

    Blog paramTest(Blog blog,String nameParam);

}
