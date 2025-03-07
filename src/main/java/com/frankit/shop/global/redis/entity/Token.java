package com.frankit.shop.global.redis.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash(value = "token")
public class Token {
    @Id
    private final String key;
    private final String refreshToken;
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private final Long expiredTime;

    @Builder
    private Token(String key, String refreshToken, Long expiredTime) {
        this.key = key;
        this.refreshToken = refreshToken;
        this.expiredTime = expiredTime;
    }

    public static Token of(String key, String refreshToken, Long expiredTime) {
        return Token.builder()
                .key(key)
                .refreshToken(refreshToken)
                .expiredTime(expiredTime)
                .build();
    }
}
