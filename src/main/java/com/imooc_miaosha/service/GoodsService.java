package com.imooc_miaosha.service;


import com.imooc_miaosha.dao.GoodsMapper;
import com.imooc_miaosha.domain.MiaoshaGoods;
import com.imooc_miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    public List<GoodsVo> listGoodsVo(){
        return goodsMapper.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goodsVo) {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goodsVo.getId());
        goodsMapper.reduceStock(miaoshaGoods);     //减库存其实就是根据商品id更新商品库存为原来库存减1，库存减1可以直接放到sql语句中实现
    }
}
