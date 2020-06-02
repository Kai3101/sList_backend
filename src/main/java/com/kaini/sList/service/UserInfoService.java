package com.kaini.sList.service;

import com.kaini.sList.domain.UserInfo;

public interface UserInfoService {

    String registerAccount(UserInfo userInfo) throws Exception;

    String loginAccount(UserInfo userInfo) throws Exception;
}
