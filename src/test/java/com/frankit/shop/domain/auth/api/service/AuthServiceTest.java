package com.frankit.shop.domain.auth.api.service;

import com.frankit.shop.domain.auth.api.service.token.TokenHandlingService;
import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.domain.auth.config.JwtProvider;
import com.frankit.shop.domain.auth.dto.AuthRequest;
import com.frankit.shop.domain.auth.dto.AuthResponse;
import com.frankit.shop.domain.auth.entity.CustomUserDetail;
import com.frankit.shop.domain.auth.entity.PrivateClaims;
import com.frankit.shop.domain.user.entity.Users;
import com.frankit.shop.global.exception.ApiException;
import com.frankit.shop.global.redis.entity.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import static com.frankit.shop.global.exception.ExceptionEnum.INVALID_TOKEN_VALUE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private TokenHandlingService<Token> tokenHandlingService;

    @InjectMocks
    private AuthService authService;


    @DisplayName("유저 정보를 가지고 토큰 값을 추출할 수 있다")
    @Test
    void signInGetToken() {
        //given
        Users users = Users.of("usertest123", RoleEnum.USER);

        AuthRequest.SignIn signIn = AuthRequest.SignIn.of("usertest123", "pass123");
        CustomUserDetail userDetail = CustomUserDetail.of(users);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtProvider.generateToken(any(PrivateClaims.class), anyLong())).thenReturn("accessToken");

        //when
        AuthResponse.Token token = authService.signIn(signIn);

        //then
        assertThat(token.getAccessToken()).isEqualTo("accessToken");
        verify(authenticationManagerBuilder, times(1)).getObject();
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider, times(2)).generateToken(any(PrivateClaims.class), anyLong());
    }

    @DisplayName("유저 정보를 없다면 예외가 발생한다")
    @Test
    void signInNotFoundUser() {
        AuthRequest.SignIn signIn = AuthRequest.SignIn.of("usertest123", "pass123");

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        //when & then
        assertThatThrownBy(() -> authService.signIn(signIn))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactlyInAnyOrder("AUT_404_01", "존재하지 않는 사용자입니다.");

        verify(authenticationManagerBuilder, times(1)).getObject();
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider, never()).generateToken(any(PrivateClaims.class), anyLong());
    }

    @DisplayName("정상적인 refreshToken 값이면 새로운 토큰을 재발급 받을 수 있다")
    @Test
    void validTokenCanReissueToken() {
        //given
        String refreshToken = "refreshToken123";
        String userEmail = "usertest123";
        Token token = Token.of(userEmail, refreshToken, 10 * 1000L);

        when(jwtProvider.getCurrentUserEmail()).thenReturn(userEmail);
        when(tokenHandlingService.findById(anyString())).thenReturn(token);
        when(jwtProvider.parseRefreshToken(anyString(), anyString())).thenReturn(PrivateClaims.of("usertest123", null));
        when(jwtProvider.generateToken(any(PrivateClaims.class), anyLong())).thenReturn("newAccessToken", "newRefreshToken");
        when(tokenHandlingService.save(any(Token.class))).thenReturn(token);

        //when
        AuthResponse.Token result = authService.reissueToken(refreshToken);

        //then
        assertThat(result.getAccessToken()).isEqualTo("newAccessToken");
        assertThat(result.getRefreshToken()).isEqualTo("newRefreshToken");

        verify(jwtProvider, times(1)).getCurrentUserEmail();
        verify(tokenHandlingService, times(1)).findById(anyString());
        verify(jwtProvider, times(1)).parseRefreshToken(anyString(), anyString());
        verify(jwtProvider, times(2)).generateToken(any(PrivateClaims.class), anyLong());
        verify(tokenHandlingService, times(1)).save(any(Token.class));
    }

    @DisplayName("정상적이지 않은 refreshToken 값이 전달되면 새로운 토큰을 재발급 받을 수 없다")
    @Test
    void invalidTokenCanNotReissueToken() {
        //given
        String refreshToken = "refreshToken123";
        String userEmail = "usertest123";
        Token token = Token.of(userEmail, refreshToken, 10 * 1000L);

        when(jwtProvider.getCurrentUserEmail()).thenReturn(userEmail);
        when(tokenHandlingService.findById(anyString())).thenReturn(token);
        when(jwtProvider.parseRefreshToken(anyString(), anyString())).thenThrow(new ApiException(INVALID_TOKEN_VALUE_ERROR));

        //when & then
        assertThatThrownBy(() -> authService.reissueToken(refreshToken))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactly("AUT_400_01", "유효하지 않은 토큰 입니다.");

        verify(jwtProvider, times(1)).getCurrentUserEmail();
        verify(tokenHandlingService, times(1)).findById(anyString());
        verify(jwtProvider, times(1)).parseRefreshToken(anyString(), anyString());
        verify(jwtProvider, never()).generateToken(any(PrivateClaims.class), anyLong());
        verify(tokenHandlingService, never()).save(any(Token.class));
    }

}