package com.frankit.shop.domain.auth.entity;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.domain.user.entity.User;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Set;

public class CustomUserDetail implements UserDetails {
    private final String email;
    private final Set<GrantedAuthority> authorities;

    @Builder
    private CustomUserDetail(String email, Set<GrantedAuthority> authorities) {
        this.email = email;
        this.authorities = authorities;
    }

    public static CustomUserDetail of(User user) {
        return CustomUserDetail.builder()
                .email(user.getEmail())
                .authorities(getGrantedAuthoritySet(user.getRole()))
                .build();
    }

    public static Set<GrantedAuthority> getGrantedAuthoritySet(RoleEnum role) {
        return Collections.singleton(new SimpleGrantedAuthority(role.getValue()));
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException();
    }
}
