package com.imooc_miaosha.controller;

import com.imooc_miaosha.domain.MiaoshaUser;
import com.imooc_miaosha.redis.RedisService;
import com.imooc_miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_list")
    public String list(Model model,MiaoshaUser user) {
//        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;//设定优先级
//        log.info("this is goodcontroller"+token);
//        MiaoshaUser user = miaoshaUserService.getByToken(response,token);
        model.addAttribute("user",user);
        System.out.println("-----------------------------222222");
        return "goods_list";
    }

}
