package com.frankit.shop.domain.auth.api.service.token;

public interface TokenHandlingService<T> {
    T save(T value);
    T findById(String key);
    void delete(String key);
}
