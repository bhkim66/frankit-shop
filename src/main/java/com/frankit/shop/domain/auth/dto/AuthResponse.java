package com.frankit.shop.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class AuthResponse {
    @Getter
    public static class Token {
        private final String accessToken;
        private final String refreshToken;
        private final LocalDateTime publishTime;

        @Builder
        private Token(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            publishTime = LocalDateTime.now();
        }

        public static Token of(String accessToken, String refreshToken) {
            return Token.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }
}
