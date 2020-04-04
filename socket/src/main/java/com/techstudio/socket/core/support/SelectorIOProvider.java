package com.techstudio.socket.core.support;

import com.techstudio.socket.core.IOProvider;
import com.techstudio.socket.util.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
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
        unRegisterSelection(channel, readSelector, inputCallbackMap);
    }

    @Override
    public void unRegisterOutput(SocketChannel channel) {
        unRegisterSelection(channel, writeSelector, outputCallbackMap);
    }

    @Override
    public void close() {
        // 如果expect已经是true，则其它线程已经执行过close操作了
        if (isClosed.compareAndSet(false, true)) {
            readExecutorPool.shutdown();
            writeExecutorPool.shutdown();

            inputCallbackMap.clear();
            outputCallbackMap.clear();

            readSelector.wakeup();
            writeSelector.wakeup();

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
            } catch (ClosedChannelException e) {
                logger.error(e.getMessage(), e);
                return null;
            } finally {
                lock.set(false);
                // 唤醒所有等待线程
                lock.notifyAll();
            }
        }
    }

    private void unRegisterSelection(SocketChannel channel, Selector selector,
                                     HashMap<SelectionKey, Runnable> callbackMap) {

        if (channel.isRegistered()) {
            SelectionKey key = channel.keyFor(selector);
            if (key != null) {
                // 取消监听的方法
                key.cancel();
                callbackMap.remove(key);
                selector.wakeup();
            }
        }
    }

    /**
     * write selector 线程，主要工作是轮询select，筛选已经可以进行写操作的channel，
     * 然后将这些channel的HandleOutputCallback交给线程池去异步调度
     */
    private void startWriteSelectorThread() {
        Thread thread = new Thread(() -> {
            while (!isClosed.get()) {
                try {
                    if (writeSelector.select() == 0) {
                        synchronized (isRegOutput) {
                            if (isRegOutput.get()) {
                                isRegOutput.wait(50);
                            }
                        }
                        // 没有已就绪的channel，继续循环
                        continue;
                    }
                    // 拿到已就绪的channel集合
                    Set<SelectionKey> selectionKeys = readSelector.selectedKeys();
                    // 遍历这些channel，将他们加入到线程池中，进行异步调度
                    for (SelectionKey key : selectionKeys) {
                        // 先判断SelectionKey是否有效
                        if (key.isValid()) {
                            // 执行异步调度
                            handleSelection(key, SelectionKey.OP_WRITE, outputCallbackMap, writeExecutorPool);
                        }
                    }
                    // 重点
                    selectionKeys.clear();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            }
        }, "write-selector-thread");

        // 设置最高优先级
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    /**
     * read selector 线程，主要工作是轮询select，筛选已经可以进行读操作的channel，
     * 然后将这些channel的HandleInputCallback交给线程池去异步调度
     */
    private void startReadSelectorThread() {
        Thread thread = new Thread(() -> {
            while (!isClosed.get()) {
                try {
                    // 特别注意：因为这是阻塞方法，期间会持有SocketImpl.publicKeys的锁，如果这时有新的channel注册，
                    // register方法依然需要持有SocketImpl.publicKeys锁才能访问同步代码块，互斥，导致注册线程一直阻塞下去
                    // 解决办法是：注册是唤醒select，并阻塞select线程，等register完成后再释放select的阻塞状态
                    if (readSelector.select() == 0) {
                        synchronized (isRegInput) {
                            if (isRegInput.get()) {
                                isRegInput.wait(50);
                            }
                        }
                        // 没有已就绪的channel，继续循环
                        continue;
                    }
                    // 拿到已就绪的channel集合
                    Set<SelectionKey> selectionKeys = readSelector.selectedKeys();
                    // 遍历这些channel，将他们加入到线程池中，进行异步调度
                    for (SelectionKey key : selectionKeys) {
                        // 先判断SelectionKey是否有效
                        if (key.isValid()) {
                            // 执行异步调度
                            handleSelection(key, SelectionKey.OP_READ, inputCallbackMap, readExecutorPool);
                        }
                    }
                    // 重点
                    selectionKeys.clear();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            }
        }, "read-selector-thread");

        // 设置最高优先级
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    /**
     * 通过线程池异步调度读写
     *
     * @param key          SelectionKey
     * @param keyOps       int
     * @param callbackMap  HashMap<SelectionKey, Runnable>
     * @param executorPool ExecutorService
     */
    private void handleSelection(SelectionKey key, int keyOps,
                                 HashMap<SelectionKey, Runnable> callbackMap,
                                 ExecutorService executorPool) {

        // 取消注册，因为此方法是通过线程池异步执行的，有可能异步线程还没有对channel进行处理，而在下一个select循环中
        // selector只要是channel在可读或可写状态时，都会被select出来，会出现重复往线程池中丢任务的情况
        // 取消对keyOps的监听
        key.interestOps(key.readyOps() & ~keyOps);
        Runnable runnable = callbackMap.get(key);
        if (runnable != null && !executorPool.isShutdown()) {
            executorPool.execute(runnable);
        }
    }
}
