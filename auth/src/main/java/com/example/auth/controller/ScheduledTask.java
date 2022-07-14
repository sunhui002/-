package com.example.auth.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScheduledTask implements Runnable{

    public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd  hh:mm:ss");

    public   int taskid ;
    public long time;

    public ScheduledTask(long time, int taskid) {
        this.time = time;
      this.taskid=taskid;

    }

    @SneakyThrows
    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        Calendar calendar=Calendar.getInstance();
        Date time = calendar.getTime();
        String starttime = simpleDateFormat.format(time);
        System.out.println(name+" taskID "+taskid+" starttime:  "+starttime);
        log.error(name+" taskID "+taskid+" starttime:  "+starttime);
        Thread.sleep(this.time);
        Calendar calendar1=Calendar.getInstance();
        Date time1 = calendar1.getTime();
        String endtime = simpleDateFormat.format(time1);
        System.out.println(name+" taskID "+taskid+" endtime:  "+endtime);
        log.error(name+" taskID "+taskid+" endtime:  "+endtime);
    }
}
