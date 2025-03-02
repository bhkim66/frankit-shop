package com.frankit.shop.domain.productoption.dto;

import com.frankit.shop.domain.productoption.common.OptionType;
import com.frankit.shop.domain.productoption.entity.ProductOption;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductOptionResponse {
    private final Long id;
    private final String optionName;
    private final OptionType optionType;
    private final int extraPrice;

    @Builder
    private ProductOptionResponse(Long id, String optionName, OptionType optionType, int extraPrice) {
        this.id = id;
        this.optionName = optionName;
        this.optionType = optionType;
        this.extraPrice = extraPrice;
    }

    public static ProductOptionResponse of(ProductOption productOption) {
        return ProductOptionResponse.builder()
                .id(productOption.getId())
                .optionName(productOption.getName())
                .optionType(productOption.getType())
                .extraPrice(productOption.getExtraPrice())
                .build();
    }

}
