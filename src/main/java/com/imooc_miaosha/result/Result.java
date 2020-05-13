package com.imooc_miaosha.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候,如果是null的对象,key也会消失
public class Result<T> implements Serializable {
    int code;
    T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private Result(T data) {
        this.data = data;
    }

    @JsonIgnore
    //使之不在json序列化结果当中
    public boolean isSuccess() {
        return this.code == 200;
    }

    public static <T> Result<T> Success(T data) {
        return new Result(200,data);
    }
}
