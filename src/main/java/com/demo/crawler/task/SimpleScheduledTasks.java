package com.demo.crawler.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
public class SimpleScheduledTasks {

    // 固定频率执行任务
    @Scheduled(fixedRate = 5000) // 每隔5秒执行一次
    public void fixedRateTask() {
        System.out.println("Fixed Rate Task: " + LocalDateTime.now());
    }

    // 任务执行完成后等待5秒再执行
    @Scheduled(fixedDelay = 5000) // 每隔5秒执行一次
    public void fixedDelayTask() {
        System.out.println("Fixed Delay Task: " + LocalDateTime.now());
    }

    // 使用 Cron 表达式执行任务
    @Scheduled(cron = "0/10 * * * * ?") // 每10秒执行一次
    public void cronTask() {
        System.out.println("Cron Task: " + LocalDateTime.now());
    }
}
