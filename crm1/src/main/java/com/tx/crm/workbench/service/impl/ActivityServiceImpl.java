package com.tx.crm.workbench.service.impl;

import com.tx.crm.settings.dao.UserDao;
import com.tx.crm.settings.domain.User;
import com.tx.crm.utils.PrintJson;
import com.tx.crm.utils.SqlSessionUtil;
import com.tx.crm.vo.PageinationVO;
import com.tx.crm.workbench.dao.ActivityDao;
import com.tx.crm.workbench.dao.ActivityRemarkDao ;
import com.tx.crm.workbench.domain.Activity;
import com.tx.crm.workbench.domain.ActivityRemark;
import com.tx.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public boolean save(Activity a) {
        boolean flag = true;

        int count = activityDao.save(a);

        if(count!=1){
            flag = false;
        }
        return flag;
    }

    public PageinationVO<Activity> pageList(Map<String, Object> map) {

        //取得total
        int total = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //创建一个vo对象，将total和dataList封装到vo中
         PageinationVO<Activity> vo = new PageinationVO<Activity>();
         vo.setTotal(total);
         vo.setDataList(dataList);

        //将vo返回
        return vo;
    }

    public boolean delete(String[] ids) {
        boolean flag = true;

        //查询出需要删除的备注数量
         int count1 = activityRemarkDao.getCountByAids(ids);

        //删除备注，返回收到影响的条数（实际删除的数量）
         int count2 = activityRemarkDao.deleteByAids(ids);

         if(count1!=count2){
             flag = false;
         }

        //删除市场活动
        int count3 = activityDao.delete(ids);
        if(count3!=ids.length){
            flag = false;
        }

        return flag;
    }

    public Map<String, Object> getUserListAndActivity(String id) {
        //取uList和a
        List<User> uList = userDao.getUserList();

        List<Activity> a = activityDao.getById(id);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uList",uList);
        map.put("a",a);

        return map;
    }

    public boolean update(Activity a) {

        boolean flag = false;
        int count = activityDao.update(a);

        if(count>0){
            flag = true;
        }

        return flag;
    }

    public Activity detail(String id) {

        Activity a = activityDao.detail(id);

        return a;
    }

    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);

        return arList;
    }

    public boolean deleteRemark(String id) {

        boolean flag = false;
        int count = activityRemarkDao.deleteRemark(id);

        if(count>0){
            flag = true;
        }

        return flag;
    }

    public boolean saveRemark(ActivityRemark ar) {

        boolean flag = false;

        int count = activityRemarkDao.saveRemark(ar);

        if(count>0){
            flag = true;
        }
        return flag;
    }

    public List<Activity> getActivityListByClueId(String id) {

        List<Activity> alist = activityDao.getActivityListByClueId(id);

        return alist;
    }

    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {

        List<Activity> alist = activityDao.getActivityListByNameAndNotByClueId(map);

        return alist;
    }

    public List<Activity> getActivityListByName(String aname) {


        List<Activity> alist = activityDao.getActivityListByName(aname);

        return alist;
    }
}
