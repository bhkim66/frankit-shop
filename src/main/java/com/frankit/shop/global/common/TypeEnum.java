package com.frankit.shop.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeEnum {
    Y("true"),
    N("false"),

    ;

    private final String value;
}
