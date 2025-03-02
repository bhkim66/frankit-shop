package com.frankit.shop.domain.productoption.dto;

import com.frankit.shop.domain.productoption.common.OptionType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.frankit.shop.domain.productoption.common.OptionType.I;
import static org.assertj.core.api.Assertions.assertThat;

class ProductOptionRequestTest {
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

    @DisplayName("옵션 값이 빈값이여서 예외가 발생한다")
    @Test
    void optionNameEmpty() {
        //given
        String optionName = "";
        OptionType optionType = I;
        int extraPrice = 0;

        ProductOptionRequest productOptionRequest = ProductOptionRequest.of(optionName, optionType, extraPrice);
        Set<ConstraintViolation<ProductOptionRequest>> validates = validatorFromFactory.validate(productOptionRequest);

        //when & then
        assertThat(validates).extracting(ConstraintViolation::getMessage).containsExactly("옵션명은 필수입니다.");
    }

    @DisplayName("옵션 타입이 빈값이여서 예외가 발생한다")
    @Test
    void optionTypeNotNull() {
        //given
        String optionName = "신상품";
        OptionType optionType = null;
        int extraPrice = 0;

        ProductOptionRequest productOptionRequest = ProductOptionRequest.of(optionName, optionType, extraPrice);
        Set<ConstraintViolation<ProductOptionRequest>> validates = validatorFromFactory.validate(productOptionRequest);

        //when & then
        assertThat(validates).extracting(ConstraintViolation::getMessage).containsExactly("옵션 타입은 필수입니다.");
    }

    @DisplayName("옵션 추가 금액은 0이상 100,000,000원 이하가 아니라면 예외가 발생한다")
    @Test
    void optionExtraPrice() {
        //given
        String optionName = "신상품";
        OptionType optionType = I;
        int extraPrice = -1000;

        ProductOptionRequest productOptionRequest = ProductOptionRequest.of(optionName, optionType, extraPrice);
        Set<ConstraintViolation<ProductOptionRequest>> validates = validatorFromFactory.validate(productOptionRequest);

        //when & then
        assertThat(validates).extracting(ConstraintViolation::getMessage).containsExactly("옵션 가격은 0원 이상 100,000,000원 이하입니다.");
    }

}