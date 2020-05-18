package com.imooc_miaosha.controller;



import com.imooc_miaosha.domain.UserInfo;
import com.imooc_miaosha.redis.RedisService;
import com.imooc_miaosha.redis.UserKey;
import com.imooc_miaosha.result.Result;
import com.imooc_miaosha.service.UserInfoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static javafx.scene.input.KeyCode.T;

@Controller
@RequestMapping("/demo")
public class SampleController {


    @Autowired
    UserInfoService userInfoService;

    @Autowired
    RedisService redisService;

    @RequestMapping("thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","yq");
        return "hello";
    }

    @RequestMapping("/do/get")
    @ResponseBody
    public UserInfo GetId(@Param("id") Integer id){
        return userInfoService.getId(id);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<UserInfo> RedisGet(){
        UserInfo userInfo = redisService.get(UserKey.getById,""+1,UserInfo.class);
        return Result.success(userInfo);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> RedisSet(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername("11111");
        redisService.set(UserKey.getById,""+1,UserInfo.class);
        return Result.success(true);
    }

}
