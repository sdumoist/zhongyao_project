package com.xpxp.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 简易任务队列，存放待执行的算法任务。
 */
public class TaskQueue {

    private static final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    /**
     * 添加任务
     */
    public static void addTask(Runnable task) {
        queue.offer(task);
    }

    /**
     * 获取任务（阻塞）
     */
    public static Runnable takeTask() throws InterruptedException {
        return queue.take();
    }
}
