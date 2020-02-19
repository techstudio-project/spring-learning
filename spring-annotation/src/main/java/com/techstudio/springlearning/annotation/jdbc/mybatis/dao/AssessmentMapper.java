package com.techstudio.springlearning.annotation.jdbc.mybatis.dao;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author lj
 * @date 2020/2/19
 */
public interface AssessmentMapper {

    List<Map<String, Object>> findAll();

    @Select("select * from ses_assessment_stat where `month`='2020-01'")
    List<Map<String,Object>> findAll1();
}
