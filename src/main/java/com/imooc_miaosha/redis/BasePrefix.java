package com.imooc_miaosha.redis;

//为避免redis在多人查询时key被覆盖，加prefix
public abstract class BasePrefix implements KeyPrefix {

    private int expireSconds;

    private String prefix;

    public BasePrefix(String prefix) {//0代表永不过期
        this(0,prefix);
    }

    public BasePrefix(int expireSconds, String prefix) {
        this.expireSconds = expireSconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {//默认0代表永不过期
        return expireSconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}
