package com.tx.crm.workbench.service.impl;

import com.tx.crm.utils.PrintJson;
import com.tx.crm.utils.SqlSessionUtil;
import com.tx.crm.vo.PageinationVO;
import com.tx.crm.workbench.dao.ActivityDao;
import com.tx.crm.workbench.domain.Activity;
import com.tx.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);


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
}
