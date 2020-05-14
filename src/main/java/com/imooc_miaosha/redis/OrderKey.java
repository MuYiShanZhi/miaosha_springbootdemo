package com.imooc_miaosha.redis;

public class OrderKey extends BasePrefix{
    public OrderKey(int expireSconds, String prefix) {
        super(expireSconds, prefix);
    }
}
