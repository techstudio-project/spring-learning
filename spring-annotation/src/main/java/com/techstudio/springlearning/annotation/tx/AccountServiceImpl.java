package com.techstudio.springlearning.annotation.tx;

import com.techstudio.springlearning.annotation.jdbc.mybatis.dao.AccountMapper;
import com.techstudio.springlearning.annotation.jdbc.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author lj
 * @date 2020/2/25
 */
//@Service
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;

    private final TransactionTemplate transaction;

    @Autowired
    public AccountServiceImpl(AccountMapper accountMapper, TransactionTemplate transaction) {
        this.accountMapper = accountMapper;
        this.transaction = transaction;
    }

    private void test() {
        Account one = new Account();
        one.setId(1);
        one.setMoney(100L);
        accountMapper.updateByPrimaryKey(one);
        // 模拟异常
        //int i = 10 / 0;

        Account two = new Account();
        two.setId(2);
        two.setMoney(200L);
        accountMapper.updateByPrimaryKey(two);
    }

    /**
     * 基于TransactionTemplate的编程式事务，在基于声明式不能满足需求的情况下可以用此方法来达到更细粒度的控制
     * 不过一般还是采取声明式的事务，关键是代码侵入小，更简洁
     */
    @Override
    public void transactionalTemplateTest() {
        transaction.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    test();
                }
                catch (Exception e) {
                    status.setRollbackOnly();

                    System.out.println("事务出现异常，执行回滚");
                }
            }
        });
    }

    /**
     * 基于aop的事务,一个注解就可以搞定
     */
    @Override
    @Transactional
    public void transactionalAop() {
        test();
    }
}
