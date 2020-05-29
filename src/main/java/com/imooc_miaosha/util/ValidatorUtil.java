package com.imooc_miaosha.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//这是一个用于判断格式的类
public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");  //定义规则：以1开头，后面跟10个数字

    public static boolean isMobile(String src){
        if (StringUtils.isEmpty(src)){
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(src);
        return matcher.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("16812352278"));    //true
        System.out.println(isMobile("1568472238"));     //false
    }
}
