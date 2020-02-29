package com.techstudio.springlearning.annotation.jdbc.mybatis.dao;

import com.techstudio.springlearning.annotation.jdbc.entity.Account;

/**
 * @author lj
 * @date 2020/2/25
 */
public interface AccountMapper {

    int insert(Account account);

    int updateByPrimaryKey(Account account);

}
