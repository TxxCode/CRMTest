package com.tx.crm.workbench.dao;

import com.tx.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue c);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String[] id);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getPageListByCondition(Map<String, Object> map);

    List<Clue> getClueById(String id);

    int updateClue(Clue c);
}
