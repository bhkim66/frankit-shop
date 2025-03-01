package com.frankit.shop.domain.product.service;

import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.dto.ProductResponse;
import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductRepository;
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

import static java.util.Optional.*;
import static org.assertj.core.api.Assertions.*;
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
        ProductRequest productRequest = ProductRequest.of("컴퓨터", "컴퓨터입니다", 1_000_000, 5000);

        //when
        Product savedProduct = productService.createProduct(productRequest);

        //then
        assertThat(savedProduct.getName()).isEqualTo("컴퓨터");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @DisplayName("하나의 상품을 수정할 수 있다")
    @Test
    void updateProduct() {
        //given
        Product product = createProductStep("컴퓨터", "컴퓨터입니다", 1_000_000, 5000);

        when(productRepository.findById(anyLong())).thenReturn(of(product));

        //when
        ProductRequest productRequest = ProductRequest.of("신형 컴퓨터", "더 비싸진 컴퓨터입니다", 1_500_000, 5000);
        Product result = productService.updateProduct(1L, productRequest);

        //then
        assertThat(result).extracting("name", "description", "price")
                .containsExactly("신형 컴퓨터", "더 비싸진 컴퓨터입니다", 1_500_000);
        verify(productRepository, times(1)).findById(anyLong());
    }

    @DisplayName("하나의 상품을 수정하려 할 때 찾는 상품이 없으면 예외가 발생한다")
    @Test
    void updateNotExistProduct() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(empty());

        //when
        ProductRequest productRequest = ProductRequest.of("신형 컴퓨터", "더 비싸진 컴퓨터입니다", 1_500_000, 5000);
        assertThatThrownBy(() -> productService.updateProduct(1L, productRequest)).hasMessageContaining("해당 상품이 존재하지 않습니다.");

        //then
        verify(productRepository, times(1)).findById(anyLong());
    }

    @DisplayName("하나의 상품을 삭제할 수 있다")
    @Test
    void deleteProduct() {
        //given
        Product product = createProductStep("컴퓨터", "컴퓨터입니다", 1_000_000, 5000);

        when(productRepository.findById(anyLong())).thenReturn(of(product));
        doNothing().when(productRepository).delete(any(Product.class));

        //when
        productService.deleteProduct(1L);

        //then
        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @DisplayName("하나의 상품을 삭제 하는데 상품이 없다면 예외가 발생한다")
    @Test
    void deleteNotExistProduct() {
        //given
        Long productId = 1L;

        when(productRepository.findById(anyLong())).thenReturn(empty());

        //when
        assertThatThrownBy(() -> productService.deleteProduct(productId)).hasMessageContaining("해당 상품이 존재하지 않습니다.");

        //then
        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, never()).delete(any(Product.class));
    }

    private static Product createProductStep(String productName, String productDescription, int price, int deliveryFee) {
        return Product.create(productName, productDescription, price, deliveryFee);
    }
}
