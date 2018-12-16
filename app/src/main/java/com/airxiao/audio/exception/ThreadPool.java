package com.airxiao.audio.exception;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class ThreadPool {

    private static ExecutorService cachedThreadPool;
    private static byte[] ThreadPoolLock = new byte[] {};

    // 提交线程
    public static Future<?> submit(Runnable mRunnable) {
        synchronized (ThreadPoolLock) {
            if (cachedThreadPool == null) {
                cachedThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {
                    int  i = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("ThreadPool_" + i);
                        return thread;
                    }
                });
            }
        }
        return cachedThreadPool.submit(mRunnable);
    }

    // 关闭
    public static void shutdown() {
        if (cachedThreadPool != null && !cachedThreadPool.isShutdown())
            cachedThreadPool.shutdown();
        cachedThreadPool = null;
    }

}
