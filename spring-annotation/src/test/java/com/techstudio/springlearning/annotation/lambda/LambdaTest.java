package com.techstudio.springlearning.annotation.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author lj
 * @date 2020/1/18
 */
public class LambdaTest {

    public static void main(String[] args) {
        Consumer<Void> a = aVoid -> System.out.println("lambda test");
        a.accept(null);

        Consumer<String> s = System.out::println;
        s.accept("hello");
        s.accept("world");

        food food = (p1, p2, p3) ->
                System.out.println("cook ok,食材：" + p1 + "," + p2 + "," + p3);
        food.cook("白菜", "胡萝卜", "土豆");
        food.cook("白菜1", "胡萝卜2", "土豆3");

        List<String> lists = new ArrayList<>();
        lists.forEach(list -> System.out.println(""));

        Predicate<String> predicate = s1 -> s1.equals("");
        predicate = predicate.or(s1 -> s1.equals("12"));
        predicate = predicate.or(s1 -> s1.equals("123"));

        Predicate<String> predicate1 = s1 -> s1.equals("")
                || s1.equals("12")
                || s1.equals("123");

        lists.stream().filter(predicate);
        lists.stream().filter(predicate1);
    }

    public void print() {
        System.out.println("lambda test");
    }

    @FunctionalInterface
    private interface food {
        void cook(String p1, String p2, String p3);

        // void eat(String p);
    }
}
