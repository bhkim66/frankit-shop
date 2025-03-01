package com.frankit.shop.domain.product.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRequestTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validatorFromFactory;

    @BeforeAll
    static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validatorFromFactory = validatorFactory.getValidator();
    }

    @AfterAll
    static void afterAll() {
        validatorFactory.close();
    }

    @DisplayName("상품 이름이 빈값이여서 오류가 발생한다")
    @Test
    void validateProductNameEmpty() {
        //given
        String productName = "";
        String productDescription = "상품설명입니다";
        int price = 3000;
        int deliveryFee = 1000;

        ProductRequest productRequest = ProductRequest.of(productName, productDescription, price, deliveryFee);
        Set<ConstraintViolation<ProductRequest>> violations = validatorFromFactory.validate(productRequest);

        // Then
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactly("상품명은 필수입니다.");
    }

    @DisplayName("상품 가격이 0원 이하일 때 오류가 발생한다")
    @Test
    void validateProductPriceLess0() {
        //given
        String productName = "신상품";
        String productDescription = "상품설명입니다";
        int price = -1;
        int deliveryFee = 1000;

        ProductRequest productRequest = ProductRequest.of(productName, productDescription, price, deliveryFee);
        Set<ConstraintViolation<ProductRequest>> violations = validatorFromFactory.validate(productRequest);

        // Then
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactly("상품 가격은 0원 이상 100,000,000원 이하입니다.");
    }


}