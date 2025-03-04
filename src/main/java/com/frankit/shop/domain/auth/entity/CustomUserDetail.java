package com.frankit.shop.domain.auth.entity;

import com.frankit.shop.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class CustomUserDetail implements UserDetails {
    private final String email;
    private final Set<GrantedAuthority> authorities;

    @Builder
    private CustomUserDetail(String email, SimpleGrantedAuthority authorities) {
        this.email = email;
        this.authorities = Collections.singleton(authorities);
    }

    public static CustomUserDetail of(User user) {
        return CustomUserDetail.builder()
                .email(user.getEmail())
                .authorities(new SimpleGrantedAuthority(user.getRole().getValue()))
                .build();
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException();
    }


}
