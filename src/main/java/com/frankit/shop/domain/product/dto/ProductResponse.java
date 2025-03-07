package com.frankit.shop.domain.product.dto;

import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.productoption.dto.ProductOptionResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProductResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final int price;
    private final int deliveryFee;
    private final LocalDateTime createdAt;
    private final List<ProductOptionResponse> options;

    @Builder

    private ProductResponse(Long id, String name, String description, int price, int deliveryFee, LocalDateTime createdAt, List<ProductOptionResponse> options) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.createdAt = createdAt;
        this.options = options;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .deliveryFee(product.getDeliveryFee())
                .options(product.getProductOptions().stream().map(ProductOptionResponse::of).toList())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
