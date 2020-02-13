package com.techstudio.springlearning.annotation.jvm;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * java 语言的一些语法糖，伪泛型、自动装箱、拆箱、遍历循环
 *
 * @author lj
 * @date 2020/1/27
 */
public class GenericTest {
    public void test() {
        Map<String, String> map = new HashMap<>();
        map.put("string", "string");
        System.out.println(map.get("string"));
    }

    /**
     * 伪泛型，导致类型擦除后，下面两个方法的签名一样，导致编译出错
     *
     * @param args
     */
//    public String method(List<String> args) {
//        return "123";
//    }
    public String method(List<Integer> args) {
        return "123";
    }

    public void unboxing() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        int sum = 0;
        for (Integer i : list) {
            sum = sum + i;
        }
        System.out.println(sum);
    }

    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        // -128~127有缓存
        System.out.println(c == d);// true
        System.out.println(e == f);// false
        System.out.println(c == (a + b));// true
        System.out.println(c.equals(a + b));// true
        System.out.println(g == (a + b));// true
        // equals不处理数据转型的关系
        System.out.println(g.equals(a + b));//false
    }
}
