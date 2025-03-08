package com.frankit.shop.global.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProductCondition extends Condition {
    private int startPrice;
    private int endPrice;

    public static ProductCondition of(String name, LocalDateTime createdAt, int startPrice, int endPrice) {
        return ProductCondition.builder()
                .name(name)
                .createdAt(createdAt)
                .startPrice(startPrice)
                .endPrice(endPrice)
            .build();
    }
}
