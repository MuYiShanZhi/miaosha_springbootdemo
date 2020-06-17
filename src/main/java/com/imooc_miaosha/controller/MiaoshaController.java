package com.imooc_miaosha.controller;

import com.imooc_miaosha.domain.MiaoshaOrder;
import com.imooc_miaosha.domain.MiaoshaUser;
import com.imooc_miaosha.domain.OrderInfo;
import com.imooc_miaosha.result.CodeMsg;
import com.imooc_miaosha.result.Result;
import com.imooc_miaosha.service.GoodsService;
import com.imooc_miaosha.service.MiaoshaService;
import com.imooc_miaosha.service.OrderService;
import com.imooc_miaosha.vo.GoodsVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(value = "商品秒杀Controller",tags = {"秒杀功能实现"})
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

//    @ApiOperation(value = "秒杀的实现")
//    @RequestMapping("/do_miaosha")
//    @ApiResponses(value = { @ApiResponse(code = 1000, message = "成功"), @ApiResponse(code = 1001, message = "失败"),
//            @ApiResponse(code = 1002, message = "缺少参数") })
    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> miaosha(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId){
        model.addAttribute("user", user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断秒杀库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //进行秒杀步骤：减库存 创建普通订单 创建秒杀订单    注意这是个事务操作
        OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVo);
        return Result.success(orderInfo);
    }
}
