package com.imooc_miaosha.service;


import com.imooc_miaosha.controller.LoginController;
import com.imooc_miaosha.dao.MiaoshaUserMapper;
import com.imooc_miaosha.domain.MiaoshaUser;
import com.imooc_miaosha.exception.GlobalException;
import com.imooc_miaosha.redis.MiaoshaUserKey;
import com.imooc_miaosha.redis.RedisService;
import com.imooc_miaosha.result.CodeMsg;
import com.imooc_miaosha.util.MD5Util;
import com.imooc_miaosha.util.Message;
import com.imooc_miaosha.util.UUIDUtil;
import com.imooc_miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserMapper miaoshaUserMapper;

    @Autowired
    RedisService redisService;

//    @Autowired
//    MyRedisUtil myRedisUtil;

    public boolean Send(String phone, HttpSession session) {
        String messageCode = String.valueOf((int)(Math.random()*100000));
        System.out.println(messageCode);
        Message message = new Message();
        message.SendSms(phone,messageCode,session);
        return true;
    }



    public MiaoshaUser getById(long id){
        return miaoshaUserMapper.getById(id);
    }

    public boolean checkCode(HttpServletResponse response,String phone) {
        if(phone == null){
            throw new GlobalException(CodeMsg.MOBILE_EMPTY);
        }
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(phone));
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response,token,user);
        return true;
    }

    public boolean login(HttpServletResponse response,LoginVo loginVo) {
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);        //出现异常直接往外抛
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response,token,user);
        return true;
    }

    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        if (StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(miaoshaUser != null){
            addCookie(response,token,miaoshaUser);
        }
        addCookie(response,token,miaoshaUser);
        return miaoshaUser;
    }
    public void addCookie(HttpServletResponse response, String token ,MiaoshaUser user){
        redisService.set(MiaoshaUserKey.token,token,user);
        log.info("生成了token");
        redisService.set(MiaoshaUserKey.token, token, user); //将user和token绑定并存入Redis中
        log.info("将user和token绑定并存入Redis中");
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);   //根据token生成cookie
        log.info("根据token生成cookie");
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie); //将cookie放入response客户端中
    }
//
//    public boolean login(HttpServletResponse response, LoginVo loginVo){      //应该返回表达业务方法含义的类型，而不应该是CodeMsg
//        if (loginVo == null){
//            throw new GlobalException(CodeMsg.SERVER_ERROR);        //出现异常直接往外抛
//        }
//        String mobile = loginVo.getMobile();
//        String formPass = loginVo.getPassword();       //通过ajax做第一次md5加密后的密码
//        //判断数据库中是否存在该手机号
//        MiaoshaUser user = getById(Long.parseLong(mobile));    //由于MiaoshaUserDao中的getById方法用到的入参为long类型，所以此处需要先转为long类型
//        if (user == null){
//            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
//        }
//        //验证密码
//        String dbPass = user.getPassword();
//        String saltDB = user.getSalt();
//        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
//        if (!calcPass.equals(dbPass)){
//            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
//        }
//        //生成cookie
//        String token = UUIDUtil.uuid();     //没必要每次都生成一个新的token，所以这里拿了出来
//        addCookie(response, token, user);
//        return true;
//    }
//
//    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
//        if (StringUtils.isEmpty(token)){
//            return null;
//        }
//        MiaoshaUser user = myRedisUtil.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
//        //延长有效期，保证有效期总是最后一次访问时间再加上session过期时间
//        if (user != null){
//            addCookie(response, token, user);      //重新往缓存中设置token，并生成新的cookie，这样就达到了延长有效期的目的
//        }
//        return user;
//    }
//
//    public void addCookie(HttpServletResponse response, String token, MiaoshaUser user){
//        myRedisUtil.set(MiaoshaUserKey.token, token, user); //将user和token绑定并存入Redis中
//        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);   //根据token生成cookie
//        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
//        cookie.setPath("/");
//        response.addCookie(cookie); //将cookie放入response客户端中
//    }
}
