package com.frankit.shop.global.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.redis")
public class RedisProperties {
    private int port;
    private String host;
    private String password;
}
