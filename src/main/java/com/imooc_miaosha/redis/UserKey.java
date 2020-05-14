package com.imooc_miaosha.redis;

public class UserKey extends BasePrefix{

    private UserKey(int expireSconds, String prefix) {
        super(expireSconds, prefix);
    }

    public static UserKey()
}
