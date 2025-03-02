package com.frankit.shop.domain.productoption.dto;

import com.frankit.shop.domain.productoption.common.OptionType;
import com.frankit.shop.domain.productoption.entity.ProductOption;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductOptionRequest {
    private final String name;
    private final OptionType type;
    private final int extraPrice;

    @Builder
    private ProductOptionRequest(String name, OptionType type, int extraPrice) {
        this.name = name;
        this.type = type;
        this.extraPrice = extraPrice;
    }

    public static ProductOptionRequest of(String name, OptionType type, int price) {
        return ProductOptionRequest.builder()
                .name(name)
                .type(type)
                .extraPrice(price)
                .build();
    }

    public ProductOption toEntity() {
        return ProductOption.builder()
                .name(name)
                .type(type)
                .extraPrice(extraPrice)
                .build();
    }
}
