package com.example.auth.filter;

import com.example.auth.Entity.MessageResult;
import com.example.auth.controller.LoginController;
import org.elasticsearch.client.ml.EvaluateDataFrameRequest;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

//@Configuration
@WebFilter(filterName = "AuthFilter",urlPatterns = "/api/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req=(HttpServletRequest) request;
        String token = request.getParameter("token");


        if(req.getRequestURI().contains("login")||req.getRequestURI().contains("register")) {
            chain.doFilter(req,resp);
            return;
        }

        if(!Optional.ofNullable(token).isPresent()){
            resp.addHeader("token","nihao");
            resp.sendRedirect("/login.html");//绝对路径也可以，相对路径有/和与/
            return;
         }
        token=URLDecoder.decode(token);
        if(!Optional.ofNullable(LoginController.cache.getIfPresent(token)).isPresent()){

            response.getOutputStream().write("登录失败".getBytes(StandardCharsets.UTF_8));
            return;
        }

        chain.doFilter(request,response);
    }
}
