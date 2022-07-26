package com.example.auth.utils;

import org.springframework.context.ApplicationContext;

public class ApplicationContextUtil {

    public static ApplicationContext applicationContext;

    public static void  set(ApplicationContext applicationContext){
        ApplicationContextUtil.applicationContext=applicationContext;
    }

}
