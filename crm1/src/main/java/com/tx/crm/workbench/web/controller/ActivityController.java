package com.tx.crm.workbench.web.controller;

import com.tx.crm.settings.domain.User;
import com.tx.crm.settings.service.UserService;
import com.tx.crm.settings.service.impl.UserServiceImpl;
import com.tx.crm.utils.DateTimeUtil;
import com.tx.crm.utils.PrintJson;
import com.tx.crm.utils.ServiceFactory;
import com.tx.crm.utils.UUIDUtil;
import com.tx.crm.vo.PageinationVO;
import com.tx.crm.workbench.domain.Activity;
import com.tx.crm.workbench.service.ActivityService;
import com.tx.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {


    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入市场活动控制器");
        String path = request.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(request,response);
        }else if("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }
    }

    protected void pageList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到查询市场活动信息列表的操作（结合条件查询+分页查询）");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
            前端需要：市场活动信息列表
            查询的总条数

            业务层拿到了两项信息后，做返回
            map.put("dataList":datalist);
            map.put("total":total);
            PrintJSON map -->json
            {"total":100,"dataList":[{市场活动1},{2}{3}]}


            vo
            PageinationVO<T>
                private int total;
                private List<T> dataList;

             PageinationVO<Activity> vo = new PageintationVO<>;
             vo.setTotal(total);
             vo.setDataList(dataList);
             PrintJSON vo -->json
            {"total":100,"dataList":[{市场活动1},{2}{3}]}
           将来分页查询，每个模块都有，所以我们选择使用一个通用vo，操作起来比较方便。
        * */
        PageinationVO<Activity> vo = as.pageList(map);


        // vo-->{"total":100,"dataList":[{市场活动1},{2}{3}]}
        PrintJson.printJsonObj(response,vo);
    }

    protected void getUserList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("获取用户信息");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response,uList);

    }

    protected void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("执行市场活动添加操作");

       String id = UUIDUtil.getUUID();
       String owner = request.getParameter("owner");
       String name = request.getParameter("name");
       String startDate = request.getParameter("startDate");
       String endDate = request.getParameter("endDate");
       String cost = request.getParameter("cst");
       String description = request.getParameter("description");

       //当前系统时间
       String createTime = DateTimeUtil.getSysTime();
       //创建人，当前登录用户
       String createBy = ((User)request.getSession().getAttribute("user")).getName();

       Activity a = new Activity();

       a.setId(id);
       a.setCost(cost);
       a.setStartDate(startDate);
       a.setOwner(owner);
       a.setName(name);
       a.setEndDate(endDate);
       a.setDescription(description);
       a.setCreateTime(createTime);
       a.setCreateBy(createBy);

       ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
       boolean flag = as.save(a);

       PrintJson.printJsonFlag(response,flag);
    }
}
