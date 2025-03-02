package com.frankit.shop.domain.product.entity;

import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.global.common.TypeEnum;
import com.frankit.shop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static com.frankit.shop.global.common.TypeEnum.*;

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

    @Enumerated(EnumType.STRING)
    private TypeEnum delYn;

    @Builder
    private Product(String name, String description, int price, int deliveryFee) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.delYn = N;
    }

    public static Product create(String name, String description, int price, int deliveryFee) {
        return Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .deliveryFee(deliveryFee)
                .build();
    }

    public Product update(ProductRequest productRequest) {
        this.name = productRequest.getName();
        this.description = productRequest.getDescription();
        this.price = productRequest.getPrice();
        this.deliveryFee = productRequest.getDeliveryFee();
        return this;
    }

    public Product delete() {
        this.delYn = Y;
        return this;
    }
}
