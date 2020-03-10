package com.techstudio.springlearning.jpa.dao;

import com.techstudio.springlearning.jpa.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lj
 * @date 2020/3/4
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
