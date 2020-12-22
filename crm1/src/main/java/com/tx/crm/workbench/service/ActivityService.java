package com.tx.crm.workbench.service;

import com.tx.crm.vo.PageinationVO;
import com.tx.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {

    boolean save(Activity a);

    PageinationVO<Activity> pageList(Map<String, Object> map);
}
