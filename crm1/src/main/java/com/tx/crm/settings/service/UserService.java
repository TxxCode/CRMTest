package com.tx.crm.settings.service;

import com.tx.crm.exception.LoginException;
import com.tx.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;
    List<User> getUserList();
}
