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
import com.tx.crm.workbench.domain.ActivityRemark;
import com.tx.crm.workbench.service.ActivityService;
import com.tx.crm.workbench.service.impl.ActivityServiceImpl;
import org.apache.log4j.xml.SAXErrorHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserAndActivity(request,response);
        }else if("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(request,response);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if("/workbench/activity/saveRemark.do".equals(path)){
            saveReamrk(request,response);
        }
    }

    private void saveReamrk(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行添加备注操作");

        String noteContent = request.getParameter("noteContent");
        String activityid = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag ="0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityid);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.saveRemark(ar);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response,map);

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("删除备注操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemark(id);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据市场活动id，取得备注信息列表");

        String activityId = request.getParameter("activityId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response,arList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
        System.out.println("进入到跳转到详细信息页面操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = as.detail(id);

        request.setAttribute("a",a);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);



    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");

        //当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //创建人，当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();

        a.setId(id);
        a.setCost(cost);
        a.setStartDate(startDate);
        a.setOwner(owner);
        a.setName(name);
        a.setEndDate(endDate);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.update(a);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入查询用户信息列表和根据市场活动id查询单条查询记录的操作");

        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //前端需要的，管业务层去要
        //uList
        // a
        //两项信息系复用率不高，我们使用map打包这两项信息即可
        Map<String,Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response,map);
    }

    protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("执行市场活动的删除操作");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        String[] ids = request.getParameterValues("id");

        boolean flag = as.delete(ids);

        PrintJson.printJsonFlag(response,flag);
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
       String cost = request.getParameter("cost");
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
