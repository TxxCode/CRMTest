package com.tx.crm.web.filter;

import com.tx.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登录的过滤器");

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        //判断进入的jsp页面是否是登录页面，如果是直接放行，不是再判断是否登录
        //判断进入的是否是login.do，如果是直接放行。
        String path = request.getServletPath();
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            chain.doFilter(req,resp);


        } //其他资源必须验证是否登录
        else{
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            //如果user不为null，说明登录过
            if(user!=null) {
                chain.doFilter(req,resp);
            }else{
                //重定向到登录页面

                //为什么使用重定向，使用转发不行吗？
                //转发之后，路径会停留在老得路径上，而不是跳转之后的新路径
                //我们应该为用户跳转到登录页面的同时，浏览器的地址栏也自动设置为当前的登录页面路径。

                //设置为动态项目路径，降低耦合度，提高扩展力
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }
    }
}
