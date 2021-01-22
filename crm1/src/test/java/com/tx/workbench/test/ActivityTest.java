package com.tx.workbench.test;

import com.tx.crm.utils.ServiceFactory;
import com.tx.crm.utils.UUIDUtil;
import com.tx.crm.workbench.domain.Activity;
import com.tx.crm.workbench.service.ActivityService;
import com.tx.crm.workbench.service.impl.ActivityServiceImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author tx
* */
/*
* junit
* 单元测试：(多线程的，并发)
* 是未来实际项目开发中，用来代替主方法main的
*
* */
public class ActivityTest {

    @Test
    public void testSave(){

        Activity a = new Activity();
        a.setId(UUIDUtil.getUUID());
        a.setName("测试");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.save(a);
        Assert.assertEquals(flag,true);

    }

}
