package com.xpxp.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 后台任务消费者线程，不断轮询执行任务队列中的任务。
 */
@Slf4j
@Component
public class TaskConsumer implements Runnable {

    @PostConstruct
    public void init() {
        Thread thread = new Thread(this);
        thread.setName("Python-Task-Consumer");
        thread.setDaemon(true); // 设置为守护线程，JVM退出时自动关闭
        thread.start();
        log.info("任务消费者线程已启动");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Runnable task = TaskQueue.takeTask();
                log.info("开始执行任务...");
                task.run();
                log.info("任务执行完成！");
            } catch (InterruptedException e) {
                log.error("任务消费者被中断", e);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("执行任务时发生异常", e);
            }
        }
    }
}
