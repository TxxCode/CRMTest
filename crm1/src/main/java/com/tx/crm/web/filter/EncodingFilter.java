package com.tx.crm.web.filter;

import javax.servlet.Filter;
import javax.servlet.*;
import javax.servlet.ServletRequest;
import java.io.IOException;

public class EncodingFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp,FilterChain chain)throws IOException,ServletException{

        System.out.println("进入到过滤器，过滤字符编码");

        //过滤post请求中文参数乱码
        req.setCharacterEncoding("UTF-8");
        //过滤响应流响应中文乱码
        resp.setContentType("text/html;charset=utf-8");

        //将请求放行
        chain.doFilter(req,resp);
    }
}
