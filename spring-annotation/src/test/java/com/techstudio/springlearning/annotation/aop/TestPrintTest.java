package com.techstudio.springlearning.annotation.aop;

import com.techstudio.springlearning.annotation.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lj
 * @date 2020/2/13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestPrintTest {

    @Autowired
    private TestPrint testPrint;

    @Autowired
    private User user;

    @Test
    public void aopTest() {
        testPrint.doSomething();
        testPrint.doSomething1();
    }
}