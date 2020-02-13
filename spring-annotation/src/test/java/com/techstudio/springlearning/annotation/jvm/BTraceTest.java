package com.techstudio.springlearning.annotation.jvm;

import com.techstudio.springlearning.annotation.ConsoleUtils;

import java.io.IOException;

/**
 * @author lj
 * @date 2020/1/24
 */
public class BTraceTest {

    public static void main(String[] args) throws IOException {
        while (true){
            ConsoleUtils.booleanCommand("is ready");
            sum(1, 4);
        }

    }

    public static void sum(int a, int b) {
        System.out.println(a + b);
    }

    public void test(){

    }

}
