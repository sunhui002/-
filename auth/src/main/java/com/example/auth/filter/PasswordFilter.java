package com.example.auth.filter;

import com.example.auth.controller.LoginController;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Optional;

@WebFilter(filterName = "PasswordFilter",urlPatterns = "*.html")
public class PasswordFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req=(HttpServletRequest) request;
        if(req.getRequestURI().contains("login")||req.getRequestURI().contains("register")) {
            chain.doFilter(req,resp);
            return;
        }
        Cookie[] cookies = req.getCookies();
        if(Optional.ofNullable(cookies).isPresent()){
            Cookie loginCookie=null;
            for(Cookie cookie:cookies){

                if(cookie.getName().equals("token")){
                    loginCookie=cookie;
                }
            }
            if(Optional.ofNullable(loginCookie).isPresent()){
                String ifexist = LoginController.cache.getIfPresent(URLDecoder.decode(loginCookie.getValue()));
                if(Optional.ofNullable(ifexist).isPresent()){
                      chain.doFilter(req,response);
                    return;
                }
            }
        }
        resp.sendRedirect("/login.html");

    }
}
