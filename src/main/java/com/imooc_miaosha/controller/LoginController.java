package com.imooc_miaosha.controller;


import com.imooc_miaosha.domain.MiaoshaUser;
import com.imooc_miaosha.result.CodeMsg;
import com.imooc_miaosha.result.Result;
import com.imooc_miaosha.service.MiaoshaUserService;
import com.imooc_miaosha.service.UserInfoService;
import com.imooc_miaosha.util.ValidatorUtil;
import com.imooc_miaosha.vo.LoginVo;
import com.imooc_miaosha.vo.PhoneLoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import sun.security.krb5.internal.crypto.RsaMd5CksumType;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){

        log.info(loginVo.toString());

        //参数校验
//        String passInput = loginVo.getPassword();//密码是否为空
//
//        String mobile = loginVo.getMobile();//判断手机号的格式
//        if(StringUtils.isEmpty(passInput)){
//            log.info(passInput);
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if(StringUtils.isEmpty(mobile)){
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if(!ValidatorUtil.isMobile(mobile)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
        //登录
        miaoshaUserService.login(response,loginVo);
        return Result.success(true);

    }

//-------------------------------------------------------以下是手机号验证码-----------------------------------------------------------//

    @RequestMapping("/phone_login")
    public String messageCode(){
        return "phone_login";
    }

    @PostMapping("/send")
    @ResponseBody
    public Result<Boolean> sendMessage(@RequestParam String phone, HttpSession session){

         return Result.success(miaoshaUserService.Send(phone,session));

    }

    @PostMapping("/checkCode")
    @ResponseBody
    public Result<Boolean> checkCode(@Valid PhoneLoginVo phoneLoginVo, HttpSession session, HttpServletResponse response){
        if(phoneLoginVo.getMessage().equals(session.getAttribute("messageCode"))){
            miaoshaUserService.checkCode(response,phoneLoginVo.getPhone());
            System.out.println("-----------------------------");
            return Result.success(true);
        }
        else{
            return Result.error(CodeMsg.MessageCode_ERROR);
        }
    }
}
