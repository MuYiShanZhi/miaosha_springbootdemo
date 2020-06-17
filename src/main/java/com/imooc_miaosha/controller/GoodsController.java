package com.imooc_miaosha.controller;

import com.imooc_miaosha.domain.MiaoshaUser;
import com.imooc_miaosha.redis.GoodsKey;
import com.imooc_miaosha.redis.RedisService;
import com.imooc_miaosha.result.Result;
import com.imooc_miaosha.service.GoodsService;
import com.imooc_miaosha.service.MiaoshaUserService;
import com.imooc_miaosha.vo.GoodsDetailVo;
import com.imooc_miaosha.vo.GoodsVo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

//    @ApiOperation(value = "展示商品列表")
    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
//    @ApiResponses(value = { @ApiResponse(code = 1000, message = "成功"), @ApiResponse(code = 1001, message = "失败"),
//            @ApiResponse(code = 1002, message = "缺少参数") })
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        //查询商品列表，包括商品和秒杀商品
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);     //放到Model中，供前端展示使用。
//        return "goods_list";
        //手动渲染
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

//    @ApiOperation(value = "秒杀商品详情")
//    @ApiResponses(value = { @ApiResponse(code = 1000, message = "成功"), @ApiResponse(code = 1001, message = "失败"),
//            @ApiResponse(code = 1002, message = "缺少参数") })
//
//    @RequestMapping("/to_detail/{goodsId}")
//    @ApiImplicitParams({ @ApiImplicitParam(name = "goodsId",
//            value = "产品id",
//            dataType = "long",
//            paramType = "path",
//            required = true)})
//    public String detail(Model model, @ApiParam("秒杀用户对象")MiaoshaUser user,
//                         @PathVariable("goodsId") long goodsId) {
//
//        model.addAttribute("user", user);
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        model.addAttribute("goods",goods);
//
//        long startAt = goods.getStartDate().getTime();  //转化为毫秒
//        long endAt = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//
//        int miaoshaStatus = 0;  //秒杀状态
//        int remainSeconds = 0;  //距离开始秒杀还有多久
//
//        if (now < startAt){     //秒杀还没开始，倒计时
//            miaoshaStatus = 0;
//            remainSeconds = (int)((startAt - now)/1000);
//        }else if (now > endAt){ //秒杀已结束
//            miaoshaStatus = 2;
//            remainSeconds = -1;
//        }else {                 //秒杀进行中
//            miaoshaStatus = 1;
//            remainSeconds = 0;
//        }
//        model.addAttribute("miaoshaStatus", miaoshaStatus);//秒杀的状态
//        model.addAttribute("remainSeconds", remainSeconds);//秒杀还有多少秒开始
//        return "goods_detail";
//    }
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

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
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(goods);
        goodsDetailVo.setMiaoshaUser(user);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        return Result.success(goodsDetailVo);
    }


    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

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
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
//        return "goods_detail";
        //手动渲染
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }

}
