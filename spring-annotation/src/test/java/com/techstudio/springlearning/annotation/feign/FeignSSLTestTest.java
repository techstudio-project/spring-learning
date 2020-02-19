package com.techstudio.springlearning.annotation.feign;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author lj
 * @date 2020/2/18
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FeignSSLTestTest {

    @Autowired
    private FeignSSLTest feignSSLTest;

    @Test
    public void getScore() {
        JSONObject json = feignSSLTest.getScore("", "", "");
        System.out.println(json.toJSONString());
    }

}