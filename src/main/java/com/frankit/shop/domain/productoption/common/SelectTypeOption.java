package com.frankit.shop.domain.productoption.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SelectTypeOption {
    S("small"),
    M("medium"),
    L("large"),
    FREE("free");

    private final String value;

}
