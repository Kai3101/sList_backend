package com.kaini.sList.impl;

import com.kaini.sList.domain.UserInfo;
import com.kaini.sList.mapper.UserInfoMapper;
import com.kaini.sList.service.UserInfoService;
import com.kaini.sList.utils.Constants;
import com.kaini.sList.utils.PasswordUtils;
import com.kaini.sList.utils.UUIDGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserInfoImpl implements UserInfoService {

    private byte[] saltValue = {1, 2, 3, 4, 5, 6, 7, 8};

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public String registerAccount(UserInfo userInfo) throws Exception {
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new Exception("UserInfo cannot be null!");
        }
        // Insert userInfo if userId does not exists, else update userInfo if userId exists
        if (StringUtils.isEmpty(userInfo.getUserId())) {
            // Insert userInfo if email address does not exists, else return error message
            if (!StringUtils.isEmpty(userInfo.getEmail()) && !StringUtils.isEmpty(userInfo.getPassword())) {
                UserInfo user = userInfoMapper.checkUserEmail(userInfo.getEmail());
                if (!ObjectUtils.isEmpty(user)) {
                    throw new Exception("Email already existed!");
                } else {
                    String userId = UUIDGenerator.getUUID();
                    userInfo.setUserId(userId);
                    String encryptPwd = PasswordUtils.encrypt(userInfo.getName(), userInfo.getPassword(), saltValue);
                    userInfo.setPassword(encryptPwd);
                    userInfo.setCreatedBy(userId);
                    userInfo.setLastModifiedBy(userId);
                    userInfo.setCreatedTime(new Date());
                    userInfo.setLastModifiedTime(new Date());
                    userInfoMapper.insertSelective(userInfo);
                    return userId;
                }
            } else {
                throw new Exception("Email and password cannot be null.");
            }
        } else {
            userInfo.setLastModifiedBy(userInfo.getLastModifiedBy());
            userInfo.setLastModifiedTime(new Date());
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
            return Constants.SUCCESS;
        }
    }

    @Override
    public String loginAccount(UserInfo userInfo) throws Exception {
        if (StringUtils.isEmpty(userInfo)) {
            throw new Exception("userInfo cannot be null.");
        }
        if (!StringUtils.isEmpty(userInfo.getEmail()) && !StringUtils.isEmpty(userInfo.getPassword())) {
            UserInfo user = userInfoMapper.checkUserEmail(userInfo.getEmail());
            if (!ObjectUtils.isEmpty(user)) {
                try {
                    PasswordUtils.decrypt(user.getPassword(), userInfo.getPassword(), saltValue);
                } catch (Exception e) {
                    throw new Exception("Incorrect password!");
                }
            } else {
                throw new Exception("Incorrect username or password!");
            }
            return user.getUserId();

        } else {
            throw new Exception("Email and password cannot be empty");
        }
    }
}
