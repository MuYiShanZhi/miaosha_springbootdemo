package com.imooc_miaosha.controller;

import com.imooc_miaosha.domain.MiaoshaUser;
import com.imooc_miaosha.domain.OrderInfo;
import com.imooc_miaosha.result.CodeMsg;
import com.imooc_miaosha.result.Result;
import com.imooc_miaosha.service.GoodsService;
import com.imooc_miaosha.service.OrderService;
import com.imooc_miaosha.vo.GoodsVo;
import com.imooc_miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> detail(MiaoshaUser miaoshaUser, @RequestParam("orderId") long orderId){
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrderInfo(orderInfo);
        orderDetailVo.setGoodsVo(goodsVo);
        return Result.success(orderDetailVo);
    }
}
