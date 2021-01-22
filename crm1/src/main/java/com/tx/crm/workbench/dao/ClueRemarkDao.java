package com.tx.crm.workbench.dao;

import com.tx.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    int delete(ClueRemark clueRemark);

    int saveRemark(ClueRemark cr);

    List<ClueRemark> getRemarkListById(String id);

    int deleteRemark(String id);

    int update(ClueRemark cr);
}
