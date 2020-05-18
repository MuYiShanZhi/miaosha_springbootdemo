package com.imooc_miaosha.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@Data
@Component
@ConfigurationProperties(prefix = "redis")//可以读到application配置文件中所有以redis打头的配置
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;
    private String password;
    private int poolMaxTotal;
    private int poolMaxWait;
    private int poolMaxIdle;
    private int poolMinIdle;
//    private String host;
//    private int port;
//    private int timeout;
//    private String password;
//    private int MaxActive;
//    private int MaxWait;
//    private int MaxIdle;
//    private int MinIdle;

}
