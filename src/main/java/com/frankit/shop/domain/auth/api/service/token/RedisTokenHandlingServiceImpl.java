package com.frankit.shop.domain.auth.api.service.token;

import com.frankit.shop.global.exception.ApiException;
import com.frankit.shop.global.redis.entity.Token;
import com.frankit.shop.global.redis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.frankit.shop.global.exception.ExceptionEnum.REDIS_TOKEN_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class RedisTokenHandlingServiceImpl implements TokenHandlingService<Token> {
    private final RedisRepository redisRepository;

    @Override
    public Token save(Token value) {
        return redisRepository.save(value);
    }

    @Override
    public Token findById(String key) {
        return redisRepository.findById(key).orElseThrow(() -> new ApiException(REDIS_TOKEN_NOT_FOUND_ERROR));
    }

    @Override
    public void delete(String key) {
        redisRepository.deleteById(key);
    }
}
