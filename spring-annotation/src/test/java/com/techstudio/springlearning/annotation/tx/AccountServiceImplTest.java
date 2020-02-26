package com.techstudio.springlearning.annotation.tx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lj
 * @date 2020/2/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void transactionalTemplateTest() {
        accountService.transactionalTemplateTest();
    }


    @Test
    public void transactionalAop() {
        accountService.transactionalAop();
    }
}