package com.tx.crm.settings.web.controller;

import com.tx.crm.settings.domain.User;
import com.tx.crm.settings.service.UserService;
import com.tx.crm.settings.service.impl.UserServiceImpl;
import com.tx.crm.utils.MD5Util;
import com.tx.crm.utils.PrintJson;
import com.tx.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入用户控制器");
        String path = request.getServletPath();

        if("/settings/user/login.do".equals(path))
        {
           login(request,response);
        }
    }
    private void login(HttpServletRequest request,HttpServletResponse response) {

        System.out.println("进入验证登录操作");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码转化为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接受浏览器端的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("-----------ip"+ip);
        System.out.println(loginPwd);
        //未来业务层开发，统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{
            User user = us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);

            //如果程序执行到此处，说明业务层没有为controller抛出任何的异常
            //表示登录成功

            PrintJson.printJsonFlag(response,true);
        }catch (Exception e) {
            e.printStackTrace();
            //一旦程序执行catch块的信息,说明业务层验证登录失败，为controller抛出异常
            //表示登录失败

            //我们需要为ajax请求提供多项信息
            /*可以有两种手段来处理：
            (1)将多项信息打包成为map，将map解析为json
            (2)创建一个Vo
                    private boolean success;
                    private String msg;
             如果对于展现的信息将来还会大量使用，我们创建一个Vo对象，使用方便
             数据量少使用map比较方便
            */
            String msg = e.getMessage();

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }
}
