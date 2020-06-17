package com.imooc_miaosha.service;


import com.imooc_miaosha.dao.OrderInfoMapper;
import com.imooc_miaosha.domain.MiaoshaOrder;
import com.imooc_miaosha.domain.MiaoshaUser;
import com.imooc_miaosha.domain.OrderInfo;
import com.imooc_miaosha.redis.OrderKey;
import com.imooc_miaosha.redis.RedisService;
import com.imooc_miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
//        return orderInfoMapper.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, ""+userId+"_"+goodsId, MiaoshaOrder.class);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo) {
        //创建普通订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(user.getId());
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());     //这里是秒杀价格，而不是原价
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);     //新建订单，未支付
        long orderId = orderInfoMapper.insert(orderInfo);  //返回值是通过@SelectKey获取的
        //创建秒杀订单
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(orderId);
        orderInfoMapper.insertMiaoshaOrder(miaoshaOrder);

        redisService.set(OrderKey.getMiaoshaOrderByUidGid, ""+user.getId()+"_"+goodsVo.getId(), miaoshaOrder);
        return orderInfo;
    }
    public OrderInfo getOrderById(long orderId) {
        return orderInfoMapper.getOrderById(orderId);
    }
}
