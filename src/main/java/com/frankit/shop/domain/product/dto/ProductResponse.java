package com.frankit.shop.domain.product.dto;

import com.frankit.shop.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int deliveryFee;
    private LocalDateTime createdAt;

    @Builder
    private ProductResponse(Long id, String name, String description, int price, int deliveryFee, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.createdAt = createdAt;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .deliveryFee(product.getDeliveryFee())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
