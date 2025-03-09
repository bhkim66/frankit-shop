package com.frankit.shop.global.condition;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProductCondition extends Condition {
    private int minPrice;
    private int maxPrice;

    public static ProductCondition of(String name, LocalDate createdAt, int minPrice, int maxPrice) {
        return ProductCondition.builder()
                .name(name)
                .createdAt(createdAt)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
            .build();
    }
}
