package com.frankit.shop.domain.productoption.dto;

import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.productoption.common.OptionType;
import com.frankit.shop.domain.productoption.entity.ProductOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
public class ProductOptionRequest {
    @NotBlank(message = "옵션명은 필수입니다.")
    private String name;
    @NotNull(message = "옵션 타입은 필수입니다.")
    private OptionType type;
    @Range(min = 0, max = 100_000_000, message = "옵션 가격은 0원 이상 100,000,000원 이하입니다.")
    private int extraPrice;

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

    public ProductOption toEntity(Product product) {
        return ProductOption.builder()
                .name(name)
                .type(type)
                .product(product)
                .extraPrice(extraPrice)
                .build();
    }
}
