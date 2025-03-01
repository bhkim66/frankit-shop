package com.frankit.shop.domain.product.dto;

import com.frankit.shop.domain.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
public class ProductRequest {
    @NotBlank(message = "상품명은 필수입니다.")
    private final String name;
    @NotBlank(message = "상품 설명은 필수입니다.")
    private final String description;
    @Range(min = 0, max = 100_000_000, message = "상품 가격은 0원 이상 100,000,000원 이하입니다.")
    private final int price;
    @Range(min = 0, max = 100_000_000, message = "배송비는 0원 이상 100,000,000원 이하입니다.")
    private final int deliveryFee;

    @Builder
    private ProductRequest(String name, String description, int price, int deliveryFee) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
    }

    public static ProductRequest of(String name, String description, int price, int deliveryFee) {
        return ProductRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .deliveryFee(deliveryFee)
                .build();
    }

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .deliveryFee(deliveryFee)
                .build();
    }
}
