package com.techstudio.springlearning.jpa.dao;

import com.techstudio.springlearning.jpa.entity.Blog;
import org.springframework.data.jpa.repository.Query;

/**
 * @author lj
 * @date 2020/3/4
 */
public interface BlogRepository extends BaseRepository<Blog, Integer> {

    @Query("SELECT b FROM Blog b LEFT JOIN fetch b.articles WHERE b.id = ?1")
    Blog getBlog(Integer id);

}
