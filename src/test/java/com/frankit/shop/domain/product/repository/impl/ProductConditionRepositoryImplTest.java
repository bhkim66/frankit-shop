package com.frankit.shop.domain.product.repository.impl;

import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductRepository;
import com.frankit.shop.global.condition.ProductCondition;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ProductConditionRepositoryImplTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        Product product1 = createProductStep("m4", "m4컴퓨터입니다", 1_990_000, 5000);
        Product product2 = createProductStep("m4Pro", "m4Pro컴퓨터입니다", 2_500_000, 5000);
        Product product3 = createProductStep("galaxyBook", "삼성컴퓨터입니다", 1_500_000, 5000);
        Product product4 = createProductStep("lgMini", "엘지컴퓨터입니다", 1_200_000, 5000);
        Product product5 = createProductStep("iPhone", "아이폰입니다", 2_010_000, 5000);
        Product product6 = createProductStep("galaxy25", "갤럭시25입니다", 1_500_000, 3000);

        productRepository.saveAll(List.of(product1, product2, product3, product4, product5, product6));
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("상품 이름으로 제품을 검색할 수 있다")
    @Test
    void findByProductName() {
        //given
        Pageable page = PageRequest.of(0, 10);
        ProductCondition condition = ProductCondition.of("m4", null, 0, 0);

        //when
        Page<Product> result = productRepository.findByProductsWithCondition(page, condition);

        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting("name", "description", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("m4", "m4컴퓨터입니다", 1_990_000),
                        Tuple.tuple("m4Pro", "m4Pro컴퓨터입니다", 2_500_000)
                );
    }

    @DisplayName("상품 금액대 별로 검색할 수 있다")
    @Test
    void findByProductPrice() {
        //given
        Pageable page = PageRequest.of(0, 3);
        ProductCondition condition = ProductCondition.of(null, null, 1_000_000, 2_000_000);

        //when
        Page<Product> result = productRepository.findByProductsWithCondition(page, condition);

        //then
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getContent())
                .extracting("name", "description", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("m4", "m4컴퓨터입니다", 1_990_000),
                        Tuple.tuple("galaxyBook", "삼성컴퓨터입니다", 1_500_000),
                        Tuple.tuple("lgMini", "엘지컴퓨터입니다", 1_200_000)
                );
    }

    private Product createProductStep(String productName, String productDescription, int price, int deliveryFee) {
        return Product.of(productName, productDescription, price, deliveryFee);
    }
}