package com.frankit.shop.domain.productoption.common;

import com.frankit.shop.global.exception.ApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.frankit.shop.global.exception.ExceptionEnum.OPTION_TYPE_IS_NOT_EQUALS;

@Getter
@RequiredArgsConstructor
public enum OptionType {
    I("입력(INPUT) 타입"),
    S("선택(SELECT) 타입");

    private final String value;

    /**
     * 상품 옵션 타입은 기존 옵션 타입과 동일한지 체크
     */
    public static void compareTo(OptionType existType, OptionType compareType) {
        if (!existType.equals(compareType)) {
            throw new ApiException(OPTION_TYPE_IS_NOT_EQUALS);
        }
    }
}
