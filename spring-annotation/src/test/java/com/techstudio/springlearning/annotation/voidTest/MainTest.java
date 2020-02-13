package com.techstudio.springlearning.annotation.voidTest;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Void 包装类 主要是在泛型中返回 void
 *
 * @author lj
 * @date 2020/1/16
 */
public class MainTest {

    /**
     * Void 类型只能返回null
     *
     * @return null
     */
    public Void test1() {
        System.out.println("this is Void test");
        return null;
    }


    public static void main1(String[] args) {
        for (Method method : MainTest.class.getDeclaredMethods()) {
            if (method.getReturnType().equals(Void.TYPE)) {
                System.out.println(method.getName());
            }
        }
    }

    @Test
    public void test2() throws ExecutionException, InterruptedException {
        FutureTask<Void> futureTask = new FutureTask<Void>(() -> {
            Thread.sleep(3000);
            // return Thread.currentThread().getName() + " task is finished";
            System.out.println(Thread.currentThread().getName() + " task is finished");
            return null;
        });

        Thread thread = new Thread(futureTask);
        thread.start();

        while (!futureTask.isDone()) {
            System.out.println(Thread.currentThread().getName() + " task is running");
            Thread.sleep(300);
        }

//        String result = futureTask.get();
//        System.out.println(result);

    }


}
