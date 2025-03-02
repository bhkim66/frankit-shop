package com.frankit.shop.domain.productoption.entity;

import com.frankit.shop.domain.productoption.common.OptionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OptionType type;

    private String name;

    private int extraPrice;

    @Builder
    private ProductOption(String name, OptionType type, int extraPrice) {
        this.name = name;
        this.type = type;
        this.extraPrice = extraPrice;
    }

    public static ProductOption create(String name, OptionType type, int price) {
        return ProductOption.builder()
                .name(name)
                .type(type)
                .extraPrice(price)
                .build();
    }
}
