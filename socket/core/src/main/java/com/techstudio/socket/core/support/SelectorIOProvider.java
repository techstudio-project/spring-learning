package com.techstudio.socket.core.support;

import com.techstudio.socket.core.IOProvider;
import com.techstudio.socket.core.util.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lj
 * @since 2020/4/4
 */
public class SelectorIOProvider implements IOProvider {

    private static final Logger logger = LoggerFactory.getLogger(SelectorIOProvider.class);
    private final AtomicBoolean isClosed = new AtomicBoolean(false);

    private final Selector readSelector;
    private final Selector writeSelector;

    private final ExecutorService readExecutorPool;
    private final ExecutorService writeExecutorPool;

    private final HashMap<SelectionKey, Runnable> inputCallbackMap = new HashMap<>(512);
    private final HashMap<SelectionKey, Runnable> outputCallbackMap = new HashMap<>(512);

    private final AtomicBoolean isRegInput = new AtomicBoolean(false);
    private final AtomicBoolean isRegOutput = new AtomicBoolean(false);

    public SelectorIOProvider() throws IOException {
        readSelector = Selector.open();
        writeSelector = Selector.open();

        readExecutorPool = Executors.newFixedThreadPool(4,
                new CustomThreadFactory("read-pool-thread-"));
        writeExecutorPool = Executors.newFixedThreadPool(4,
                new CustomThreadFactory("write-pool-thread-"));

        startReadSelectorThread();
        startWriteSelectorThread();
    }

    @Override
    public boolean registerInput(SocketChannel channel, HandleInputCallback inputCallback) {
        return registerSelection(channel, readSelector, SelectionKey.OP_READ, isRegInput,
                inputCallbackMap, inputCallback) != null;
    }

    @Override
    public boolean registerOutput(SocketChannel channel, HandleOutputCallback outputCallback) {
        return registerSelection(channel, writeSelector, SelectionKey.OP_WRITE, isRegOutput,
                outputCallbackMap, outputCallback) != null;
    }

    @Override
    public void unRegisterInput(SocketChannel channel) {
        unRegisterSelection(channel, readSelector, inputCallbackMap, isRegInput);
    }

    @Override
    public void unRegisterOutput(SocketChannel channel) {
        unRegisterSelection(channel, writeSelector, outputCallbackMap, isRegOutput);
    }

    @Override
    public void close() {
        // 如果expect已经是true，则其它线程已经执行过close操作了
        if (isClosed.compareAndSet(false, true)) {
            readExecutorPool.shutdown();
            writeExecutorPool.shutdown();

            inputCallbackMap.clear();
            outputCallbackMap.clear();

            // 关闭时会进行唤醒操作
            CloseableUtils.close(logger, readSelector, writeSelector);
        }
    }

    /**
     * 注册SocketChannel的读、写事件
     *
     * @param channel     SocketChannel
     * @param selector    Selector
     * @param registerOps int
     * @param callbackMap HashMap<SelectionKey, Runnable>
     * @param runnable    Runnable
     * @return SelectionKey （can be null）
     */
    private static SelectionKey registerSelection(SocketChannel channel, Selector selector,
                                                  int registerOps, AtomicBoolean lock,
                                                  HashMap<SelectionKey, Runnable> callbackMap,
                                                  Runnable runnable) {
        synchronized (lock) {
            // 标识已经有线程在进行注册
            lock.set(true);
            try {
                // 唤醒select，这一步很重要，要不然后面register会被阻塞
                selector.wakeup();

                SelectionKey key = null;
                // 已经注册
                if (channel.isRegistered()) {
                    key = channel.keyFor(selector);
                    if (key != null) {
                        // 添加新关注事件
                        key.interestOps(key.readyOps() | registerOps);
                    }
                }
                // 新注册
                if (key == null) {
                    // 注册selector得到Key
                    key = channel.register(selector, registerOps);
                }

                // 注册回调
                callbackMap.putIfAbsent(key, runnable);

                return key;
            } catch (ClosedChannelException
                    | CancelledKeyException
                    | ClosedSelectorException ignore) {
                return null;
            } finally {
                lock.set(false);
                // 唤醒所有等待线程
                lock.notifyAll();
            }
        }
    }

    private void unRegisterSelection(SocketChannel channel, Selector selector,
                                     HashMap<SelectionKey, Runnable> callbackMap, AtomicBoolean lock) {

        synchronized (lock) {
            lock.set(true);
            selector.wakeup();
            try {
                if (channel.isRegistered()) {
                    SelectionKey key = channel.keyFor(selector);
                    if (key != null) {
                        // 取消监听的方法
                        key.cancel();
                        callbackMap.remove(key);
                    }
                }
            } finally {
                lock.set(false);
                try {
                    lock.notifyAll();
                } catch (Exception ignore) {
                }
            }
        }
    }

    /**
     * write selector 线程，主要工作是轮询select，筛选已经可以进行写操作的channel，
     * 然后将这些channel的HandleOutputCallback交给线程池去异步调度
     */
    private void startWriteSelectorThread() {
        SelectThread writeSelect = new SelectThread("write-selector-thread",
                isClosed, writeSelector, writeExecutorPool, outputCallbackMap, isRegOutput, SelectionKey.OP_WRITE);
        writeSelect.start();
    }

    /**
     * read selector 线程，主要工作是轮询select，筛选已经可以进行读操作的channel，
     * 然后将这些channel的HandleInputCallback交给线程池去异步调度
     */
    private void startReadSelectorThread() {
        SelectThread readSelect = new SelectThread("read-selector-thread",
                isClosed, readSelector, readExecutorPool, inputCallbackMap, isRegInput, SelectionKey.OP_READ);
        readSelect.start();
    }

    static class SelectThread extends Thread {
        private final AtomicBoolean isClosed;
        private final Selector selector;
        private final ExecutorService executorPool;
        private final Map<SelectionKey, Runnable> callbackMap;
        private final AtomicBoolean lock;
        private final int keyOps;

        public SelectThread(String threadName,
                            AtomicBoolean isClosed,
                            Selector selector,
                            ExecutorService executorPool,
                            Map<SelectionKey, Runnable> callbackMap,
                            AtomicBoolean lock, int keyOps) {
            super(threadName);
            this.isClosed = isClosed;
            this.selector = selector;
            this.executorPool = executorPool;
            this.callbackMap = callbackMap;
            this.lock = lock;
            this.keyOps = keyOps;
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public void run() {
            AtomicBoolean lock = this.lock;
            AtomicBoolean isClosed = this.isClosed;
            Selector selector = this.selector;
            Map<SelectionKey, Runnable> callbackMap = this.callbackMap;
            ExecutorService executorPool = this.executorPool;
            int keyOps = this.keyOps;

            while (!isClosed.get()) {
                try {
                    if (selector.select() == 0) {
                        waitSelection(lock);
                        // 没有已就绪的channel，继续循环
                        continue;
                    } else if (lock.get()) {
                        waitSelection(lock);
                    }
                    // 拿到已就绪的channel集合
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();

                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    // 遍历这些channel，将他们加入到线程池中，进行异步调度
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        // 先判断SelectionKey是否有效
                        if (selectionKey.isValid()) {
                            // 执行异步调度
                            handleSelection(selectionKey, keyOps, callbackMap, executorPool, lock);
                        }
                        // 重点
                        iterator.remove();
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                } catch (ClosedSelectorException ignore) {
                    break;
                }
            }
        }

        private static void waitSelection(final AtomicBoolean lock) {
            synchronized (lock) {
                if (lock.get()) {
                    try {
                        lock.wait(50);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage(), e);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        /**
         * 通过线程池异步调度读写
         *
         * @param key          SelectionKey
         * @param keyOps       int
         * @param callbackMap  HashMap<SelectionKey, Runnable>
         * @param executorPool ExecutorService
         */
        private static void handleSelection(SelectionKey key,
                                            int keyOps,
                                            Map<SelectionKey, Runnable> callbackMap,
                                            ExecutorService executorPool,
                                            AtomicBoolean lock) {

            // 取消注册，因为此方法是通过线程池异步执行的，有可能异步线程还没有对channel进行处理，而在下一个select循环中
            // selector只要是channel在可读或可写状态时，都会被select出来，会出现重复往线程池中丢任务的情况
            // 取消对keyOps的监听
            synchronized (lock) {
                try {
                    key.interestOps(key.readyOps() & ~keyOps);
                } catch (CancelledKeyException e) {
                    return;
                }
            }
            Runnable runnable = callbackMap.get(key);
            if (runnable != null && !executorPool.isShutdown()) {
                executorPool.execute(runnable);
            }
        }
    }
}
