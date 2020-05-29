package com.imooc_miaosha.vo;

import com.imooc_miaosha.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
@Data
public class PhoneLoginVo {
    @NotNull
    @IsMobile
    private String phone;
    @NotNull
    @Length(min= 5)
    private String message;

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + phone + '\'' +
                ", password='" + message + '\'' +
                '}';
    }
}
