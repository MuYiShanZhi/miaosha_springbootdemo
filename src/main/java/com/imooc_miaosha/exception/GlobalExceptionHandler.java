package com.imooc_miaosha.exception;


import com.imooc_miaosha.result.CodeMsg;
import com.imooc_miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
        e.printStackTrace();
        if (e instanceof GlobalException){
            GlobalException ex = (GlobalException)e;//强转
            return Result.error(ex.getCodeMsg());
        }else if (e instanceof BindException){//判断是否为绑定异常
            BindException ex = (BindException)e;    //强转
            List<ObjectError> errors = ex.getAllErrors();   //获取所有的错误
            ObjectError error = errors.get(0);      //拿到第一个错误
            String msg = error.getDefaultMessage();     //拿到错误信息
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));      //拼接完之后的message应该是 参数校验异常：msg的内容
        }else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
