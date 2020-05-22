package com.imooc_miaosha.service;


import com.imooc_miaosha.dao.UserInfoMapper;
import com.imooc_miaosha.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    public UserInfo getId(Integer id){
        return userInfoMapper.selectByPrimaryKey(id);
    }

    public Integer add(UserInfo userInfo){
        return userInfoMapper.insert(userInfo);
    }
}
