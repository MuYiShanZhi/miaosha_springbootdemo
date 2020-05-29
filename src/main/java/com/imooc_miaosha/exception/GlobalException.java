package com.imooc_miaosha.exception;


import com.imooc_miaosha.result.CodeMsg;
import lombok.Data;

@Data
public class GlobalException extends RuntimeException {

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.codeMsg = codeMsg;
    }


}
