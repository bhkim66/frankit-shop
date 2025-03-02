package com.frankit.shop.domain.productoption.entity;

import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.productoption.common.OptionType;
import com.frankit.shop.domain.productoption.dto.ProductOptionRequest;
import com.frankit.shop.global.common.TypeEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.frankit.shop.global.common.TypeEnum.Y;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;

    @Enumerated(EnumType.STRING)
    private OptionType type;

    private int extraPrice;

    @Enumerated(EnumType.STRING)
    private TypeEnum delYn;

    @Builder
    private ProductOption(Product product, String name, OptionType type, int extraPrice) {
        this.product = product;
        this.name = name;
        this.type = type;
        this.extraPrice = extraPrice;
    }

    public static ProductOption create(Product product, String name, OptionType type, int price) {
        return ProductOption.builder()
                .product(product)
                .name(name)
                .type(type)
                .extraPrice(price)
                .build();
    }

    public ProductOption delete() {
        this.delYn = Y;
        return this;
    }

    public ProductOption update(ProductOptionRequest productOptionRequest) {
        this.name = productOptionRequest.getName();
        this.type = productOptionRequest.getType();
        this.extraPrice = productOptionRequest.getExtraPrice();
        return this;
    }
}
