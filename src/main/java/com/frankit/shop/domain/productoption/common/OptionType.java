package com.frankit.shop.domain.productoption.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OptionType {
    I("입력(INPUT) 타입"),
    S("선택(SELECT) 타입");

    private final String value;
}
