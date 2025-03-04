package com.frankit.shop.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthRequest {

    @Getter
    @NoArgsConstructor
    public static class SignIn {
        private String email;
        private String password;

        @Builder
        private SignIn(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public static SignIn of(String email, String password) {
            return SignIn.builder()
                    .email(email)
                    .password(password)
                    .build();
        }

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class RefreshToken {
        private String refreshToken;
    }
}
