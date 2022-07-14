package com.example.auth.controller;


import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Controller
@RequestMapping("/scheduled")
public class ScheduledController {

    public  static ThreadPoolTaskScheduler threadPoolTaskScheduler=new ThreadPoolTaskScheduler();

    static {
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.initialize();
    }
  public  static int  taskid=0;
    @GetMapping ("/add")
    public void add(){

        //每天固定时间的定时任务被阻塞，会延迟，不会不执行。
        //线程池中的复用线程每次执行完当前任务(自定义run方法),就会去延时队列中获取任务，一旦时间差小0就会执行当前任务，所以任务不会因为超过时间就不执行。
        if(taskid==0){
            threadPoolTaskScheduler.schedule(new ScheduledTask(180000,taskid++ ), new CronTrigger("0/5 * * * * ? "));
        }
else  threadPoolTaskScheduler.schedule(new ScheduledTask(10000,taskid++ ), new CronTrigger("0 4 0,17 1/1 * ? "));

    }
}
