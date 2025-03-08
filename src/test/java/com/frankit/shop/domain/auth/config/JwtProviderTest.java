package com.frankit.shop.domain.auth.config;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.domain.auth.entity.PrivateClaims;
import com.frankit.shop.domain.auth.handler.JwtHandler;
import com.frankit.shop.global.exception.ApiException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.Set;

import static com.frankit.shop.domain.auth.common.RoleEnum.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtProviderTest {
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        JwtHandler jwtHandler = new JwtHandler();
        jwtProvider = new JwtProvider(jwtHandler);
    }

    @DisplayName("정상적인 토큰을 생성할 수 있다")
    @Test
    void generateToken() throws BadRequestException {
        //given
        String accessToken = createToken("usertest123", Set.of(USER), 10 * 1000L);
        //when
        Authentication authentication = jwtProvider.validateTokenReturnAUthentication(accessToken);
        //then
        assertThat(authentication.getPrincipal())
                .extracting("email")
                .isEqualTo("usertest123");
    }

    @DisplayName("유효하지 않는 토큰은 예외가 발생한다")
    @Test
    void failToValidateWithInvalidToken() {
        //given
        String accessToken = createToken("usertest123", Set.of(USER), 30 * 1000L);

        //when & then
        assertThatThrownBy(() ->jwtProvider.validateTokenReturnAUthentication(accessToken + "fail"))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactly("AUT_400_01", "유효하지 않은 토큰 입니다.");
    }

    @DisplayName("유효기간이 지난 토큰은 예외가 발생한다")
    @Test
    void failToValidateWthExpiredToken() throws InterruptedException {
        //given
        String accessToken = createToken("usertest123", Set.of(USER),  1000L);
        Thread.sleep(2000);

        //when & then
        assertThatThrownBy(() ->jwtProvider.validateTokenReturnAUthentication(accessToken + "fail"))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactly("AUT_400_01", "유효하지 않은 토큰 입니다.");
    }

    @DisplayName("토큰을 복호화하여 토큰 정보를 가져올 수 있다")
    @Test
    void getAuthentication() {
        //given
        String accessToken = createToken("usertest123", Set.of(USER),  30 * 1000L);

        //when
        Authentication authentication = jwtProvider.validateTokenReturnAUthentication(accessToken);

        //then
        assertThat(authentication.getPrincipal())
                .extracting("email").isEqualTo("usertest123");
    }

    @DisplayName("잘못된 토큰을 복호화하여 예외가 발생한다")
    @Test
    void invalidTokenGetAuthentication() {
        //given
        String accessToken = createToken("usertest123", Set.of(USER),  30 * 1000L);

        //when & then
        assertThatThrownBy(() -> jwtProvider.validateTokenReturnAUthentication(accessToken + "fail"))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactly("AUT_400_01", "유효하지 않은 토큰 입니다.");
    }

    @DisplayName("request refreshToken 값과 저장된 값이 같으면 정상적인 값을 반환한다")
    @Test
    void validValueCanParseRefreshToken() {
        //given
        String refreshToken = createToken("usertest123", Set.of(USER),  30 * 1000L);

        //when
        PrivateClaims privateClaims = jwtProvider.parseRefreshToken(refreshToken, refreshToken);

        //then
        assertThat(privateClaims)
                .extracting("email").isEqualTo("usertest123");
    }

    @DisplayName("request refreshToken 값과 저장된 값이 같으면 다르면 예외가 발생한다")
    @Test
    void invalidValueCanNotParseRefreshToken() {
        //given
        String refreshToken = "refreshToken123";
        String storedRefreshToken = "invalidValue";

        //when
        assertThatThrownBy(() -> jwtProvider.parseRefreshToken(refreshToken, storedRefreshToken))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactly("AUT_400_01", "유효하지 않은 토큰 입니다.");
    }

    private String createToken(String email, Set<RoleEnum> role, Long expiredTime) {
        PrivateClaims claims = PrivateClaims.of(email, role);
        return jwtProvider.generateToken(claims, expiredTime);
    }
}