package com.frankit.shop.domain.auth.entity;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.domain.auth.config.JwtProvider;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Getter
public class PrivateClaims {
    private final String email;
    private final Set<RoleEnum> authority;

    @Builder
    private PrivateClaims(String email, Set<RoleEnum> authority) {
        this.email = email;
        this.authority = authority;
    }

    public static PrivateClaims of(String email, Set<RoleEnum> authority) {
        return PrivateClaims.builder()
                .email(email)
                .authority(authority)
                .build();
    }

    public static String convertType(Object o) {
        if (o instanceof String) {
            return (String) o;
        } else if (o instanceof LinkedHashMap) {
            return ((LinkedHashMap<?, ?>) o).toString();
        } else {
            throw new IllegalArgumentException("Unexpected type for USER_EMAIL claim: " + o.getClass());
        }
    }
}
