package com.example.auth.controller;

import com.example.auth.Entity.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class SessionControl {

    @RequestMapping("/add/session")
    public String setsession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("Token","1234556");
        return  "添加session"+session.getAttribute("Token");
    }

    @RequestMapping("/get/session")
    public String getsession(HttpServletRequest request){
        HttpSession session = request.getSession();
        return  "获取session"+session.getAttribute("Token");

    }
}
