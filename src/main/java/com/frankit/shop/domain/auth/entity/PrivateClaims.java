package com.frankit.shop.domain.auth.entity;

import com.frankit.shop.domain.auth.config.JwtProvider;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
public class PrivateClaims {
    private final String email;
    private final Set<GrantedAuthority> authority;

    @Builder
    private PrivateClaims(String email, Set<GrantedAuthority> authority) {
        this.email = email;
        this.authority = authority;
    }

    public static PrivateClaims of(String email, Set<GrantedAuthority> authority) {
        return PrivateClaims.builder()
                .email(email)
                .authority(authority)
                .build();
    }
}
