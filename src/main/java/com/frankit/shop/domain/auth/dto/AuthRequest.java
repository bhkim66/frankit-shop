package com.frankit.shop.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthRequest {
    @Getter
    @NoArgsConstructor
    public static class SignIn {
        @NotBlank(message = "이메일을 입력해주세요")
        @Email(message = "이메일 양식을 지켜주세요")
        private String email;
        @NotBlank(message = "비밀번호를 입력해주세요")
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
