package com.imooc_miaosha.controller;

import com.imooc_miaosha.domain.MiaoshaUser;
import com.imooc_miaosha.redis.RedisService;
import com.imooc_miaosha.service.GoodsService;
import com.imooc_miaosha.service.MiaoshaUserService;
import com.imooc_miaosha.vo.GoodsVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Api(value = "产品Cotroller",tags = {"产品售卖"})
@RequestMapping("/goods")
public class GoodsController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @ApiOperation(value = "展示商品列表")
    @RequestMapping("/to_list")
    @ApiResponses(value = { @ApiResponse(code = 1000, message = "成功"), @ApiResponse(code = 1001, message = "失败"),
            @ApiResponse(code = 1002, message = "缺少参数") })
    public String list(@ApiParam("Model")Model model, @ApiParam("秒杀用户对象") MiaoshaUser user) {
//        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;//设定优先级
//        log.info("this is goodcontroller"+token);
//        MiaoshaUser user = miaoshaUserService.getByToken(response,token);
        System.out.println("-----------------------------222222");
        model.addAttribute("user", user);
        //查询商品列表，包括商品和秒杀商品
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);     //放到Model中，供前端展示使用。
        return "goods_list";
    }

    @ApiOperation(value = "秒杀商品详情")
    @ApiResponses(value = { @ApiResponse(code = 1000, message = "成功"), @ApiResponse(code = 1001, message = "失败"),
            @ApiResponse(code = 1002, message = "缺少参数") })

    @RequestMapping("/to_detail/{goodsId}")
    @ApiImplicitParams({ @ApiImplicitParam(name = "goodsId",
            value = "产品id",
            dataType = "long",
            paramType = "path",
            required = true)})
    public String detail(Model model, @ApiParam("秒杀用户对象")MiaoshaUser user,
                         @PathVariable("goodsId") long goodsId) {

        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();  //转化为毫秒
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;  //秒杀状态
        int remainSeconds = 0;  //距离开始秒杀还有多久

        if (now < startAt){     //秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now)/1000);
        }else if (now > endAt){ //秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {                 //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);//秒杀的状态
        model.addAttribute("remainSeconds", remainSeconds);//秒杀还有多少秒开始
        return "goods_detail";
    }

}
