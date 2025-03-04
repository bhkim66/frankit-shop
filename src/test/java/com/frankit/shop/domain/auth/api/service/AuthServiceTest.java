package com.frankit.shop.domain.auth.api.service;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.domain.auth.config.JwtTokenProvider;
import com.frankit.shop.domain.auth.dto.AuthRequest;
import com.frankit.shop.domain.auth.dto.AuthResponse;
import com.frankit.shop.domain.auth.entity.CustomUserDetail;
import com.frankit.shop.domain.user.entity.User;
import com.frankit.shop.domain.user.repository.UserRepository;
import com.frankit.shop.global.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;


    @DisplayName("유저 정보를 가지고 토큰 값을 추출할 수 있다")
    @Test
    void signInGetToken() {
        //given
        User user = User.of("testerKim123", RoleEnum.USER);

        AuthRequest.SignIn signIn = AuthRequest.SignIn.of("testerKim123", "pass123");
        CustomUserDetail userDetail = CustomUserDetail.of(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(any(JwtTokenProvider.PrivateClaims.class), anyLong())).thenReturn("accessToken");

        //when
        AuthResponse.Token token = authService.signIn(signIn);

        //then
        assertThat(token.getAccessToken()).isEqualTo("accessToken");
        verify(authenticationManagerBuilder, times(1)).getObject();
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(2)).generateToken(any(JwtTokenProvider.PrivateClaims.class), anyLong());
    }

    @DisplayName("유저 정보를 없다면 예외가 발생한다")
    @Test
    void signInNotFoundUser() {
        AuthRequest.SignIn signIn = AuthRequest.SignIn.of("testerKim123", "pass123");

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
        verify(jwtTokenProvider, never()).generateToken(any(JwtTokenProvider.PrivateClaims.class), anyLong());
    }
}