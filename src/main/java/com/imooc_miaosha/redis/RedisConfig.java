package com.imooc_miaosha.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Data
@Component
@ConfigurationProperties(prefix = "redis")//可以读到application配置文件中所有以redis打头的配置
public class RedisConfig {
//    @Value("${spring.redis.host}")
    private String host;
//    @Value("${spring.redis.port}")
    private int port;
//    @Value("${spring.redis.timeout}")
    private int timeout;
//    @Value("${spring.redis.password}")
    private String password;
//    @Value("${spring.redis.pool.max-active}")
    private int maxActive;
//    @Value("${spring.redis.pool.max-wait}")
    private int maxWait;
//    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;
//    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;


}
