package com.frankit.shop.domain.product.repository;

import com.frankit.shop.domain.config.DatabaseCleanUp;
import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.entity.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        Product product1 = createProductStep("컴퓨터", "컴퓨터입니다", 1_000_000, 5000);
        Product product2 = createProductStep("휴대폰", "휴대폰입니다", 1_500_000, 5000);
        Product product3 = createProductStep("스피커", "스피커입니다", 500_000, 3000);

        productRepository.saveAll(List.of(product1, product2, product3));
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }

    @DisplayName("삭제되지 않은 모든 상품들을 조회한다")
    @Test
    void selectProducts() {
        //given
        PageRequest page = PageRequest.of(0, 10);

        //when
        Page<Product> products = productRepository.findByListIdAndDelYnN(page);

        //then
        assertThat(products.getContent()).hasSize(3)
                .extracting("name", "description", "price")
                .containsExactlyInAnyOrder(
                        tuple("컴퓨터", "컴퓨터입니다", 1_000_000),
                        tuple("휴대폰", "휴대폰입니다", 1_500_000),
                        tuple("스피커", "스피커입니다", 500_000)
                );
    }

    @DisplayName("한 상품이 삭제되어 2개 상품들을 조회한다")
    @Test
    @Transactional
    void selectTwoProducts() {
        //given
        PageRequest page = PageRequest.of(0, 10);
        productRepository.findById(1L).ifPresent(Product::delete);
        em.flush();

        //when
        Page<Product> products = productRepository.findByListIdAndDelYnN(page);

        //then
        assertThat(products.getContent()).hasSize(2)
                .extracting("name", "description", "price")
                .containsExactlyInAnyOrder(
                        tuple("휴대폰", "휴대폰입니다", 1_500_000),
                        tuple("스피커", "스피커입니다", 500_000)
                );
    }

    @DisplayName("하나의 상품을 조회힌다")
    @Test
    void selectProduct() {
        //given
        Long productId = 1L;

        //when
        Product product = productRepository.findByIdAndDelYnN(productId).orElse(null);

        //then
        assertThat(product.getName()).isEqualTo("컴퓨터");
        assertThat(product.getDescription()).isEqualTo("컴퓨터입니다");
        assertThat(product.getPrice()).isEqualTo(1_000_000);
    }

    @DisplayName("새로운 상품을 등록한다")
    @Test
    void insertProduct() {
        //given
        Product product = createProductStep("장난감", "장난감입니다", 100_000, 5000);
        Product savedProduct = productRepository.save(product);

        //when
        Product result = productRepository.findById(savedProduct.getId()).orElse(null);

        //then
        assertThat(result)
                .extracting("name", "description", "price")
                .containsExactly("장난감", "장난감입니다", 100_000);
    }

    @DisplayName("기존 상품을 수정한다")
    @Test
    void updateProduct() {
        //given
        Long productId = 1L;
        ProductRequest productRequest = ProductRequest.of("컴퓨터 ver2.0", "다음 모델 컴퓨터입니다", 2_000_000, 5000);
        Product savedProduct = productRepository.findById(productId).orElse(null);

        //when
        Product updatedProduct = savedProduct.update(productRequest);
        productRepository.save(updatedProduct);

        Product result = productRepository.findById(productId).orElse(null);

        //then
        assertThat(result).extracting("name", "description", "price")
                .containsExactlyInAnyOrder("컴퓨터 ver2.0", "다음 모델 컴퓨터입니다", 2_000_000);
    }

    private Product createProductStep(String productName, String productDescription, int price, int deliveryFee) {
        return Product.of(productName, productDescription, price, deliveryFee);
    }

}
