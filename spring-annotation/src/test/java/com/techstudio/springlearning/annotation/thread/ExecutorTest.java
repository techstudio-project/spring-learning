package com.techstudio.springlearning.annotation.thread;


import java.util.concurrent.*;

/**
 * @author lj
 * @since 2020/4/9
 */
public class ExecutorTest {

    public static void main(String[] args) {

        // 创建一个不限线程数上限的线程池，任何提交的任务都将立即执行
        Executors.newCachedThreadPool();
        // 创建固定大小的线程池
        Executors.newFixedThreadPool(10);

        Executors.newScheduledThreadPool(10);

        // 创建只有一个线程的线程池
        Executors.newSingleThreadExecutor();

        int corePoolSize; // 线程池长期维持的线程数，即使线程处于Idle状态，也不会回收。
        int maximumPoolSize; // 线程数的上限
        long keepAliveTime;
        TimeUnit unit; // 超过corePoolSize的线程的idle时长，
        // 超过这个时间，多余的线程会被回收。
        BlockingQueue<Runnable> workQueue; // 任务的排队队列
        ThreadFactory threadFactory; // 新线程的产生方式
        RejectedExecutionHandler handler; // 拒绝策略


        // 不要使用Executors.newXXXThreadPool()快捷方法创建线程池，因为这种方式会使用无界的任务队列，
        // 为避免OOM，我们应该使用ThreadPoolExecutor的构造方法手动指定队列的最大长度
        ExecutorService executorService = new ThreadPoolExecutor(2, 2,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(512), // 使用有界队列，避免OOM
                new ThreadPoolExecutor.CallerRunsPolicy()); // 直接由提交任务者执行这个任务，会导致主线程阻塞

        // executorService.execute();
        // Future future = executorService.submit() // 提交带有返回结果的任务
        Thread thread = new Thread(); // 默认线程组
        thread.start();

    }

}
