package com.example.auth;

import com.example.auth.utils.ApplicationContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@MapperScan(basePackages = "com.example.auth.Dao")
@SpringBootApplication
@ServletComponentScan(basePackages = "com.example.auth.filter")
public class AuthApplication {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(AuthApplication.class, args);
        ApplicationContextUtil.set(applicationContext);

    }

}
