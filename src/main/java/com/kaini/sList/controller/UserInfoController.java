package com.kaini.sList.controller;

import com.kaini.sList.domain.UserInfo;
import com.kaini.sList.service.UserInfoService;
import com.kaini.sList.utils.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/register")
    @ResponseBody
    public ResultBean register(@RequestBody UserInfo userInfo) throws Exception {
        String result = userInfoService.registerAccount(userInfo);
        return new ResultBean<>(result);
    }

    @RequestMapping("/login")
    @ResponseBody
    public ResultBean login(@RequestBody UserInfo userInfo) throws Exception {
        String result = userInfoService.loginAccount(userInfo);
        return new ResultBean<>(result);
    }
}