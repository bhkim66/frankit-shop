package com.frankit.shop.domain.product.entity;

import com.frankit.shop.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private int price;

    private int deliveryFee;

    @Builder
    private Product(String name, String description, int price, int deliveryFee) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
    }

    public static Product create(String name, String description, int price, int deliveryFee) {
        return Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .deliveryFee(deliveryFee)
                .build();
    }
}
