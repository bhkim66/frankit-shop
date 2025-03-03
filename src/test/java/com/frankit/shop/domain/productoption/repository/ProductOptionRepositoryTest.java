package com.frankit.shop.domain.productoption.repository;

import com.frankit.shop.domain.config.DatabaseCleanUp;
import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductRepository;
import com.frankit.shop.domain.productoption.common.OptionType;
import com.frankit.shop.domain.productoption.dto.ProductOptionRequest;
import com.frankit.shop.domain.productoption.entity.ProductOption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.frankit.shop.domain.productoption.common.OptionType.I;
import static com.frankit.shop.global.common.TypeEnum.Y;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
public class ProductOptionRepositoryTest {

    @Autowired
    ProductOptionRepository productOptionRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        Product product = createProductStep("유니폼", "유니폼입니다", 100_000, 5000);
        Product save = productRepository.save(product);

        ProductOption option1 = createProductOptionStep(save,"스몰", I, 0);
        ProductOption option2 = createProductOptionStep(save, "미디움", I, 500);
        ProductOption option3 = createProductOptionStep(save, "라지", I, 1000);

        productOptionRepository.saveAll(List.of(option1, option2, option3));
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }

    @DisplayName("한 상품에 대한 삭제되지 않은 옵션을 조회할 수 있다")
    @Test
    void selectProductOptions() {
        //given
        Long productId = 1L;

        //when
        List<ProductOption> productOptions = productOptionRepository.findProductOptions(productId);

        //then
        assertThat(productOptions).hasSize(3)
                .extracting("name", "type", "extraPrice")
                .containsExactlyInAnyOrder(
                        tuple("스몰", I, 0),
                        tuple("미디움", I, 500),
                        tuple("라지", I, 1000)
                );
    }

    @DisplayName("옵션이 존재하지 않으면 빈값을 호출한다")
    @Test
    void selectProductOptionNotFound() {
        //given
        Product product = createProductStep("유니폼2", "유니폼2입니다", 200_000, 5000);
        Product save = productRepository.save(product);
        //when
        List<ProductOption> productOptions = productOptionRepository.findProductOptions(save.getId());

        //then
        assertThat(productOptions).isEmpty();
    }

    @DisplayName("새로운 상품 옵션을 등록할 수 있다")
    @Test
    void insertProductOption() {
        //given
        Product product = createProductStep("유니폼2", "유니폼2입니다", 200_000, 5000);
        Product saveProduct = productRepository.save(product);
        ProductOption newOption = createProductOptionStep(saveProduct, "엑스트라 라지", I, 2000);
        //when
        ProductOption savedOption = productOptionRepository.save(newOption);

        //then
        assertThat(savedOption).extracting("name", "type", "extraPrice")
                .containsExactly("엑스트라 라지", I, 2000);
    }

    @DisplayName("기존 상품 옵션을 수정할 수 있다")
    @Test
    void updateProductOption() {
        //given
        Long productOptionId = 1L;
        ProductOption productOption = productOptionRepository.findProductOption(productOptionId).orElse(null);

        //when
        ProductOption updateOption = productOption.update(ProductOptionRequest.of("엑스트라 라지", I, 2000));

        //then
        assertThat(updateOption).extracting("name", "type", "extraPrice")
                .containsExactly("엑스트라 라지", I, 2000);
    }

    @DisplayName("기존 상품을 삭제할 수 있다")
    @Test
    void deleteProductOption() {
        //given
        Long productOptionId = 1L;
        ProductOption productOption = productOptionRepository.findProductOption(productOptionId).orElse(null);

        //when
        ProductOption deletedOption = productOption.delete();

        //then
        assertThat(deletedOption).extracting("delYn").isEqualTo(Y);
    }

    private static ProductOption createProductOptionStep(Product product, String optionName, OptionType optionType, int price) {
        return ProductOption.create(product, optionName, optionType, price);
    }

    private Product createProductStep(String productName, String productDescription, int price, int deliveryFee) {
        return Product.create(productName, productDescription, price, deliveryFee);
    }
}
