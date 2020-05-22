package com.imooc_miaosha.domain;

import lombok.Data;

@Data
public class UserInfo {
    private Integer id;

    private String username;

    public UserInfo() {
    }

    public UserInfo(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

}