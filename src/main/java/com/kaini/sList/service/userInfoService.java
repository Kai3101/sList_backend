package com.kaini.sList.service;

import com.kaini.sList.domain.UserInfo;

public interface userInfoService {

    String registerAccount(UserInfo userInfo) throws Exception;

    String loginAccount() throws Exception;
}
