package com.frankit.shop.global.redis.repository;

import com.frankit.shop.global.redis.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<Token, String> {
}
