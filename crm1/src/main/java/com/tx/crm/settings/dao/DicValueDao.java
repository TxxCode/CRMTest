package com.tx.crm.settings.dao;

import com.tx.crm.settings.domain.DicValue;

import java.util.List;

/**
 * Author 北京动力节点
 */
public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
