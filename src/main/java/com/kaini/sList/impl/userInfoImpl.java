package com.kaini.sList.impl;

import com.kaini.sList.domain.UserInfo;
import com.kaini.sList.mapper.UserInfoMapper;
import com.kaini.sList.service.userInfoService;
import com.kaini.sList.utils.Constants;
import com.kaini.sList.utils.UUIDGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
public class userInfoImpl implements userInfoService {

    private UserInfoMapper userInfoMapper;

    @Override
    public String registerAccount(UserInfo userInfo) throws Exception {
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new Exception("UserInfo cannot be null!");
        }
        // Insert userInfo if userId does not exists, else update userInfo if userId exists
        if (StringUtils.isEmpty(userInfo.getUserId())) {
            // Insert userInfo if email address does not exists, else return error message
            if (StringUtils.isEmpty(userInfo.getEmail())) {
                userInfo.setUserId(UUIDGenerator.getUUID());
                userInfoMapper.insertSelective(userInfo);
                return Constants.SUCCESS;
            } else {
                throw new Exception("This email address already exists.");
            }
        } else {

            return Constants.SUCCESS;
        }

    }

    @Override
    public String loginAccount() throws Exception {
        return null;
    }
}
