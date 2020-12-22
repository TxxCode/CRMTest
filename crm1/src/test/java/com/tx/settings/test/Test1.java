package com.tx.settings.test;


import com.tx.crm.settings.domain.User;
import com.tx.crm.utils.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {

    public static void main(String[] args) {
        //验证失效时间
        //失效时间
       /* String expireTime = "2019-10-1 10:10:10";
        //当前系统时间
        String currentTime = DateTimeUtil.getSysTime();

        int cout = expireTime.compareTo(currentTime);
        System.out.println(cout);*/

       //验证是否锁定
   /*  String lockState = "0";
       if("0".equals(lockState)){
           System.out.println("账号已锁定");
       }*/

       /*//浏览器端的ip地址
        String ip = "192.168.1.1";
       //允许访问的ip得知群
        String allowIps = "192.168.1.1,192.168.1.2";
        if (allowIps.contains(ip)){
            System.out.println("有效ip地址，允许访问系统");
        }else
        {
            System.out.println("受限ip地址，联系管理员处理");
        }*/

/*
        //当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人，当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();*/

/*//添加成功
						//添加成功后刷新活动信息表，局部刷新
						//清空添加操作模态窗口的数据
						//提交表单
							注意：我们拿到了form表单的jQuery对象
							对于表单jquery对象，提供了submit()方法让我们提交表单
							但jQuery没有提供reset方法，但是原生的js提供了reset方法
							所以我们要将jquery对象转化为原生dom对象

							jquery对象转化为dom对象:    jquery对象[下标]
							dom对象转化为jquery对象:     $(dom)

*/						//$("#activityAddForm")[0].reset();
    }
}
