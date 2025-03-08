package com.frankit.shop.domain.productoption.service;

import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductRepository;
import com.frankit.shop.domain.productoption.api.service.ProductOptionService;
import com.frankit.shop.domain.productoption.common.OptionType;
import com.frankit.shop.domain.productoption.dto.ProductOptionRequest;
import com.frankit.shop.domain.productoption.dto.ProductOptionResponse;
import com.frankit.shop.domain.productoption.entity.ProductOption;
import com.frankit.shop.domain.productoption.repository.ProductOptionRepository;
import com.frankit.shop.global.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.frankit.shop.domain.productoption.common.OptionType.I;
import static com.frankit.shop.domain.productoption.common.OptionType.S;
import static com.frankit.shop.global.common.TypeEnum.Y;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductOptionServiceTest {

    @Mock
    private ProductOptionRepository productOptionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductOptionService productOptionService;


    @DisplayName("3개 이하의 입력 타입 상품 옵션을 등록할 수 있다")
    @Test
    void addProductOptionsInputType() {
        //given
        Product product = createProductStep("유니폼", "유니폼입니다", 100_000, 5000);
        ProductOption option1 = ofProductOptionStep(null,"스몰", I, 0);
        ProductOption option2 = ofProductOptionStep(null, "미디움", I, 500);
        ProductOption option3 = ofProductOptionStep(null, "라지", I, 1000);

        when(productOptionRepository.saveAll(anyList())).thenReturn(List.of(option1, option2, option3));
        when(productRepository.getReferenceById(anyLong())).thenReturn(product);

        List<ProductOptionRequest> productOptionRequests = List.of(
                ProductOptionRequest.of("스몰", I, 0),
                ProductOptionRequest.of("미디움", I, 500),
                ProductOptionRequest.of("라지", I, 1000)
        );

        //when
        List<ProductOptionResponse> productOptions = productOptionService.addProductOptions(1L, productOptionRequests);

        //then
        assertThat(productOptions).hasSize(3)
                .extracting("optionName", "optionType", "extraPrice")
                .containsExactlyInAnyOrder(
                        tuple("스몰", I, 0),
                        tuple("미디움", I, 500),
                        tuple("라지", I, 1000)
                );
        verify(productOptionRepository, times(1)).saveAll(anyList());
        verify(productRepository, times(1)).getReferenceById(anyLong());
    }

    @DisplayName("3개 이하의 선택 타입 상품 옵션을 등록할 수 있다")
    @Test
    void addProductOptionsSelectType() {
        //given
        Product product = createProductStep("유니폼", "유니폼입니다", 100_000, 5000);
        ProductOption option1 = ofProductOptionStep(null,"S", S, 0);
        ProductOption option2 = ofProductOptionStep(null, "M", S, 500);
        ProductOption option3 = ofProductOptionStep(null, "L", S, 1000);

        when(productOptionRepository.saveAll(anyList())).thenReturn(List.of(option1, option2, option3));
        when(productRepository.getReferenceById(anyLong())).thenReturn(product);

        List<ProductOptionRequest> productOptionRequests = List.of(
                ProductOptionRequest.of("S", S, 0),
                ProductOptionRequest.of("M", S, 500),
                ProductOptionRequest.of("L", S, 1000)
        );

        //when
        List<ProductOptionResponse> productOptions = productOptionService.addProductOptions(1L, productOptionRequests);

        //then
        assertThat(productOptions).hasSize(3)
                .extracting("optionName", "optionType", "extraPrice")
                .containsExactlyInAnyOrder(
                        tuple("S", S, 0),
                        tuple("M", S, 500),
                        tuple("L", S, 1000)
                );
        verify(productOptionRepository, times(1)).saveAll(anyList());
        verify(productRepository, times(1)).getReferenceById(anyLong());
    }

    @DisplayName("상품의 상품 옵션 갯수가 3개를 넘으면 예외가 발생한다")
    @Test
    void addProductTotalOptionsLessThenThree() {
        //given
        ProductOption option1 = ofProductOptionStep(null, "S", S, 0);
        ProductOption option2 = ofProductOptionStep(null, "M", S, 500);
        ProductOption option3 = ofProductOptionStep(null, "L", S, 1000);

        when(productOptionRepository.findProductOptions(anyLong())).thenReturn(List.of(option1, option2, option3));

        List<ProductOptionRequest> productOptionRequests = List.of(
                ProductOptionRequest.of("XL", S, 0)
        );

        //when & then
        assertThatThrownBy(() -> productOptionService.addProductOptions(1L, productOptionRequests))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactlyInAnyOrder("OPT_400_01", "3개 이하의 상품 옵션만 등록할 수 있습니다.");

        verify(productOptionRepository, times(1)).findProductOptions(anyLong());
        verify(productOptionRepository, never()).saveAll(anyList());
    }

    @DisplayName("상품의 상품 옵션은 기존 옵션과 다르면 예외가 발생한다")
    @Test
    void addProductOptionsSameExistOptionType() {
        //given
        ProductOption option = ofProductOptionStep(null, "S", S, 0);

        when(productOptionRepository.findProductOptions(anyLong())).thenReturn(List.of(option));

        List<ProductOptionRequest> productOptionRequests = List.of(
                ProductOptionRequest.of("XL", I, 1000)
        );

        //when & then
        assertThatThrownBy(() -> productOptionService.addProductOptions(1L, productOptionRequests))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactlyInAnyOrder("OPT_400_02", "기존 옵션 타입과 다른 옵션 타입을 등록할 수 없습니다.");

        verify(productOptionRepository, times(1)).findProductOptions(anyLong());
        verify(productOptionRepository, never()).saveAll(anyList());
    }

    @DisplayName("특정 상품에 해당하는 옵션들을 조회할 수 있다")
    @Test
    void selectProductOptions() {
        //given
        ProductOption option1 = ofProductOptionStep(null,"스몰", I, 0);
        ProductOption option2 = ofProductOptionStep(null, "미디움", I, 500);
        ProductOption option3 = ofProductOptionStep(null, "라지", I, 1000);

        when(productOptionRepository.findProductOptions(anyLong())).thenReturn(List.of(option1, option2, option3));

        //when
        List<ProductOptionResponse> productOptions = productOptionService.selectProductOptions(1L);

        //then
        assertThat(productOptions).hasSize(3)
                .extracting("optionName", "optionType", "extraPrice")
                .containsExactlyInAnyOrder(
                        tuple("스몰", I, 0),
                        tuple("미디움", I, 500),
                        tuple("라지", I, 1000)
                );
        verify(productOptionRepository, times(1)).findProductOptions(anyLong());
    }

    @DisplayName("특정 상품의 하나의 옵션을 수정할 수 있다")
    @Test
    void updateProductOption() {
        //given
        ProductOption option = ofProductOptionStep(null,"스몰", I, 0);
        when(productOptionRepository.findProductOption(anyLong())).thenReturn(of(option));

        //when
        ProductOption result = productOptionService.updateProductOption(1L, ProductOptionRequest.of("미디움", I, 500));

        //then
        assertThat(result).extracting("name", "type", "extraPrice")
                .containsExactly("미디움", I, 500);

        verify(productOptionRepository, times(2)).findProductOption(anyLong());
    }

    @DisplayName("옵션을 수정할 때 기존 타입과 다르면 예외가 발생한다")
    @Test
    void updateProductOptionNotEqualsType() {
        //given
        ProductOption option = ofProductOptionStep(null,"스몰", I, 0);
        when(productOptionRepository.findProductOption(anyLong())).thenReturn(of(option));

        //when & then
        assertThatThrownBy(() -> productOptionService.updateProductOption(1L, ProductOptionRequest.of("S", S, 500)))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactlyInAnyOrder("OPT_400_02", "기존 옵션 타입과 다른 옵션 타입을 등록할 수 없습니다.");

        verify(productOptionRepository, times(1)).findProductOption(anyLong());
    }

    @DisplayName("특정 상품의 하나의 옵션을 수정하려는데 해당하는 옵션이 없으면 예외가 발생한다")
    @Test
    void updateProductOptionNotFound() {
        //given
        when(productOptionRepository.findProductOption(anyLong())).thenReturn(empty());

        //when & then
        assertThatThrownBy(() -> productOptionService.updateProductOption(1L, ProductOptionRequest.of("미디움", I, 500)))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactlyInAnyOrder("GLO_404_01", "요청한 요소를 찾을 수 없습니다.");
        verify(productOptionRepository, times(1)).findProductOption(anyLong());
    }

    @DisplayName("특정 상품의 옵션을 삭제할 수 있다")
    @Test
    void deleteProductOption() {
        //given
        ProductOption option1 = ofProductOptionStep(null,"스몰", I, 0);

        when(productOptionRepository.findProductOption(anyLong())).thenReturn(of(option1));

        //when
        ProductOption result = productOptionService.deleteProductOption(1L);

        //then
        assertThat(result).extracting("delYn").isEqualTo(Y);
        verify(productOptionRepository, times(1)).findProductOption(anyLong());
    }

    @DisplayName("특정 상품의 옵션을 삭제하려는데 해당하는 옵션이 없으면 예외가 발생한다")
    @Test
    void deleteProductOptionNotFound() {
        //given
        when(productOptionRepository.findProductOption(anyLong())).thenReturn(empty());

        //when & then
        assertThatThrownBy(() -> productOptionService.deleteProductOption(1L))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactlyInAnyOrder("GLO_404_01", "요청한 요소를 찾을 수 없습니다.");
        verify(productOptionRepository, times(1)).findProductOption(anyLong());
    }

    private Product createProductStep(String productName, String productDescription, int price, int deliveryFee) {
        return Product.of(productName, productDescription, price, deliveryFee);
    }

    private static ProductOption ofProductOptionStep(Product product, String optionName, OptionType optionType, int price) {
        return ProductOption.create(product, optionName, optionType, price);
    }
}
