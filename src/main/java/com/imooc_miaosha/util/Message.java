package com.imooc_miaosha.util;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import javax.servlet.http.HttpSession;

public class Message {

    public void SendSms(String phone, String message, HttpSession session){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4GL8PnfYoKhXTG9RfYu4", "anoSBambqVS1RBNALhAqc3M3uXp5jY");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "web实战课秒杀项目");
        request.putQueryParameter("TemplateCode", "SMS_191445104");
        request.putQueryParameter("TemplateParam", "{\"code\":"+message+"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            session.setAttribute("messageCode",message);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
