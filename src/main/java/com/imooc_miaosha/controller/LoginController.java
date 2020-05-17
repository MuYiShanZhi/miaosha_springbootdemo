package com.imooc_miaosha.controller;

import com.imooc_miaosha.domain.UserInfo;
import com.imooc_miaosha.result.Result;
import com.imooc_miaosha.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserInfoService userInfoService;

    public String toLogin(){
        return "login";
    }


}
