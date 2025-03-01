package com.frankit.shop.domain.product;

import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductRepository;
import com.frankit.shop.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @DisplayName("하나의 상품을 등록할 수 있다")
    @Test
    void createProduct() {
        //given
        String productName = "상품이름";
        String productDescription = "상품설명입니다";
        int price = 3000;
        int deliveryFee = 1000;
        Product product = Product.create(productName, productDescription, price, deliveryFee);

        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductRequest.Create productCreateRequest = ProductRequest.Create.builder()
                .name(productName)
                .description(productDescription)
                .price(price)
                .deliveryFee(deliveryFee)
                .build();

        //when
        Product savedProduct = productService.createProduct(productCreateRequest);

        //then
        assertThat(savedProduct.getName()).isEqualTo(productName);
        verify(productRepository, times(1)).save(any(Product.class));
    }



}
