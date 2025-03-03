package com.frankit.shop.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeEnum {
    Y("true"),
    N("false"),

    M("male"),
    F("female")

    ;

    private final String value;
}
