package com.frankit.shop.domain.auth.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    DEFAULT("ROLE_DEFAULT");

    private final String value;

    public static RoleEnum findByVal(String v) {
        return Arrays.stream(RoleEnum.values())
                .filter(a -> a.getValue().equals(v))
                .findAny()
                .orElse(null);
    }
}
