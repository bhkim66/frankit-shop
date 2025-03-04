package com.frankit.shop.domain.auth.config;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.domain.auth.handler.JwtHandler;
import com.frankit.shop.global.exception.ApiException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.coyote.BadRequestException;
import org.assertj.core.api.AbstractByteArrayAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static com.frankit.shop.domain.auth.common.RoleEnum.*;
import static com.frankit.shop.domain.auth.entity.CustomUserDetail.getGrantedAuthoritySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        JwtHandler jwtHandler = new JwtHandler();
        jwtTokenProvider = new JwtTokenProvider(jwtHandler);
    }

    @DisplayName("정상적인 토큰을 생성할 수 있다")
    @Test
    void generateToken() throws BadRequestException {
        //given
        String accessToken = createToken("testerKim123", USER, 10 * 1000L);
        //when
        Authentication authentication = jwtTokenProvider.validateToken(accessToken);
        //then
        assertThat(authentication.getPrincipal())
                .extracting("email")
                .isEqualTo("testerKim123");
    }

    @DisplayName("유효하지 않는 토큰은 예외가 발생한다")
    @Test
    void failToValidateWithInvalidToken() {
        //given
        String accessToken = createToken("testerKim123", USER, 30 * 1000L);

        //when
        Authentication authentication = jwtTokenProvider.validateToken(accessToken + "fail");

        //then
        assertThat(authentication).isNull();
    }

    @DisplayName("유효기간이 지난 토큰은 예외가 발생한다")
    @Test
    void failToValidateWthExpiredToken() throws InterruptedException {
        //given
        String accessToken = createToken("testerKim123", USER,  1000L);
        Thread.sleep(2000);

        //when
        Authentication authentication = jwtTokenProvider.validateToken(accessToken);

        //then
        assertThat(authentication).isNull();
    }

    @DisplayName("토큰을 복호화하여 토큰 정보를 가져올 수 있다")
    @Test
    void getAuthentication() {
        //given
        String accessToken = createToken("testerKim123", USER,  30 * 1000L);

        //when
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        //then
        assertThat(authentication.getPrincipal())
                .extracting("email").isEqualTo("testerKim123");
    }

    @DisplayName("잘못된 토큰을 복호화하여 예외가 발생한다")
    @Test
    void invalidTokenGetAuthentication() {
        //given
        String accessToken = createToken("testerKim123", USER,  30 * 1000L);

        //when & then
        assertThatThrownBy(() -> jwtTokenProvider.getAuthentication(accessToken + "fail"))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    void parseRefreshToken() {
    }

    private String createToken(String email, RoleEnum role, Long expiredTime) {
        JwtTokenProvider.PrivateClaims claims = JwtTokenProvider.PrivateClaims.of(email, getGrantedAuthoritySet(role));
        return jwtTokenProvider.generateToken(claims, expiredTime);
    }
}