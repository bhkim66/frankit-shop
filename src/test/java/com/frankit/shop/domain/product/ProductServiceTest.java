package com.frankit.shop.domain.product;

import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.dto.ProductResponse;
import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductRepository;
import com.frankit.shop.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품들을 조회할 수 있다")
    @Test
    void selectProducts() {
        //given
        Product product1 = createProductStep("컴퓨터", "컴퓨터입니다", 1_000_000, 5000);
        Product product2 = createProductStep("휴대폰", "휴대폰입니다", 1_500_000, 5000);
        Product product3 = createProductStep("스피커", "스피커입니다", 500_000, 3000);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(product1, product2, product3)));

        //when
        Pageable page = PageRequest.of(0, 10);
        Page<ProductResponse> products = productService.getProducts(page);

        //then
        assertThat(products.getContent()).hasSize(3)
                .extracting("name", "description", "price")
                .containsExactlyInAnyOrder(
                        tuple("컴퓨터", "컴퓨터입니다", 1_000_000),
                        tuple("휴대폰", "휴대폰입니다", 1_500_000),
                        tuple("스피커", "스피커입니다", 500_000)
                );
        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }

    @DisplayName("하나의 상품을 등록할 수 있다")
    @Test
    void createProduct() {
        //given
        Product product = createProductStep("컴퓨터", "컴퓨터입니다", 1_000_000, 5000);

        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductRequest.Create productCreateRequest = ProductRequest.Create.of("컴퓨터", "컴퓨터입니다", 1_000_000, 5000);

        //when
        Product savedProduct = productService.createProduct(productCreateRequest);

        //then
        assertThat(savedProduct.getName()).isEqualTo("컴퓨터");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @DisplayName("하나의 상품을 수정할 수 있다")
    @Test
    void test() {
        //given

        //when

        //then
    }

    private static Product createProductStep(String productName, String productDescription, int price, int deliveryFee) {
        return Product.create(productName, productDescription, price, deliveryFee);
    }


}
