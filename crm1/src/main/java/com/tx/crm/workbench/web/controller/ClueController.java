package com.tx.crm.workbench.web.controller;

import com.tx.crm.settings.domain.User;
import com.tx.crm.settings.service.UserService;
import com.tx.crm.settings.service.impl.UserServiceImpl;
import com.tx.crm.utils.DateTimeUtil;
import com.tx.crm.utils.PrintJson;
import com.tx.crm.utils.ServiceFactory;
import com.tx.crm.utils.UUIDUtil;
import com.tx.crm.vo.PageinationVO;
import com.tx.crm.workbench.domain.*;
import com.tx.crm.workbench.service.ActivityService;
import com.tx.crm.workbench.service.ClueService;
import com.tx.crm.workbench.service.impl.ActivityServiceImpl;
import com.tx.crm.workbench.service.impl.ClueServiceImpl;

import javax.rmi.CORBA.Util;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.PrinterGraphics;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author tx
 */
public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器");

        String path = request.getServletPath();

        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/clue/getCluePageList.do".equals(path)){
            getClueList(request,response);
        }else if("/workbench/clue/getUserListAndClue.do".equals(path)){
            getUserListAndClue(request,response);
        }else if("/workbench/clue/updateClue".equals(path)){
            updateClue(request,response);
        }else if("/workbench/clue/delete.do".equals(path)){
            delete(request,response);
        }else if("/workbench/clue/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if("/workbench/clue/getRemarkListById.do".equals(path)){
            getRemarkListById(request,response);
        }else if("/workbench/clue/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if("/workbench/clue/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);
        }else if("/workbench/clue/unbund.do".equals(path)){
            unbund(request,response);
        }else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){
            getActivityListByNameAndNotByClueId(request,response);
        }else if("/workbench/clue/bund.do".equals(path)){
            bund(request,response);
        }else if("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else if("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索转换操作");

        String cludId = request.getParameter("id");
        //是否创建交易的标记
        String flag = request.getParameter("flag");
        //如果创建交易
        if("a".equals(flag)){
            //交易
            String
        }


    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查询）");

        String aname = request.getParameter("aname");

        ActivityService as =(ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> alist = as.getActivityListByName(aname);

        PrintJson.printJsonObj(response,alist);

    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动和线索关联");

        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(cid,aids);

        PrintJson.printJsonFlag(response,flag);


    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行关联市场活动列表模糊查询");

        String aName = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<String, String>();
        map.put("aname",aName);
        map.put("clueId",clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> alist = as.getActivityListByNameAndNotByClueId(map);

        PrintJson.printJsonObj(response,alist);

    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("接触线索与市场活动关联");

        String id  = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索Id查询相关联的市场活动");

        String id = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> alist = as.getActivityListByClueId(id);

        PrintJson.printJsonObj(response,alist);


    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行修改线索备注操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        ClueRemark cr = new ClueRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setEditBy(editBy);
        cr.setEditTime(editTime);
        cr.setEditFlag(editFlag);

        boolean flag = cs.updateRemark(cr);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("cr",cr);

        PrintJson.printJsonObj(response,map);


    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行删除线索备注操作");

        String id = request.getParameter("id");

        ClueService cs =(ClueService)ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.deleteRemark(id);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListById(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入查询备注列表");

        String id = request.getParameter("id");

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        List<ClueRemark> cList = cs.getRemarkListById(id);

        PrintJson.printJsonObj(response,cList);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行添加线索备注操作");

        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String editFlag = "0";
        String clueId = request.getParameter("clueId");

        ClueRemark cr = new ClueRemark();
        cr.setClueId(clueId);
        cr.setCreateBy(createBy);
        cr.setId(id);
        cr.setCreateTime(createTime);
        cr.setEditFlag(editFlag);
        cr.setNoteContent(noteContent);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.saveRemark(cr);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("c",cr);

        PrintJson.printJsonObj(response,map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行线索删除操作");

        String[] id = request.getParameterValues("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.delete(id);

        PrintJson.printJsonFlag(response,flag);

    }

    private void updateClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行修改操作");

        String id = request.getParameter("id");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setState(state);
        c.setSource(source);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setEditTime(editTime);
        c.setEditBy(editBy);
        c.setContactSummary(contactSummary);
        c.setCompany(company);
        c.setAppellation(appellation);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.updateClue(c);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("修改数据获取");

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        String id = request.getParameter("id");

        Map<String,Object> map = new HashMap<String, Object>();
        map = cs.getUserListAndClue(id);

        PrintJson.printJsonObj(response,map);

    }

    private void getClueList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索搜索操作");

        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String mphone = request.getParameter("mphone");
        String owner = request.getParameter("owner");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        int pageNo = Integer.valueOf(request.getParameter("pageNo"));
        int pageSize =Integer.valueOf(request.getParameter("pageSize"));

        //计算需要略过的数量
        int skipCount = (pageNo-1)*pageSize;


        Map<String,Object> map = new HashMap<String,Object>();
        map.put("fullname", fullname);
        map.put("company", company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("owner",owner);
        map.put("state",state);
        map.put("source",source);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        PageinationVO<Clue> vo = cs.getCluePageList(map);

        PrintJson.printJsonObj(response,vo);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("跳转到线索详细信息页");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue c = cs.detail(id);

        request.setAttribute("c", c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);


    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行线索添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setState(state);
        c.setSource(source);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setContactSummary(contactSummary);
        c.setCompany(company);
        c.setAppellation(appellation);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);

    }

}




































