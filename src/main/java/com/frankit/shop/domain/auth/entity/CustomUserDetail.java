package com.frankit.shop.domain.auth.entity;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.domain.user.entity.User;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetail implements UserDetails {
    private final String email;
    private final String password;
    private final Set<GrantedAuthority> authorities;

    @Builder
    private CustomUserDetail(String email, String password, Set<GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static CustomUserDetail of(User user) {
        return CustomUserDetail.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(getGrantedAuthoritySet(user.getRole()))
                .build();
    }

    public static Set<GrantedAuthority> getGrantedAuthoritySet(RoleEnum role) {
        return Collections.singleton(new SimpleGrantedAuthority(role.getValue()));
    }

    public String getEmail() {
        return email;
    }

    public Set<RoleEnum> getStringAuthorities() {
        return this.authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(RoleEnum::valueOf)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPassword() {
        return password;
    }
}
