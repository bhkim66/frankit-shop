package com.frankit.shop.domain.auth.api.service;

import com.frankit.shop.domain.auth.config.JwtProvider;
import com.frankit.shop.domain.auth.dto.AuthRequest;
import com.frankit.shop.domain.auth.dto.AuthResponse;
import com.frankit.shop.domain.auth.entity.CustomUserDetail;
import com.frankit.shop.domain.auth.entity.PrivateClaims;
import com.frankit.shop.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.frankit.shop.global.exception.ExceptionEnum.USERNAME_NOT_FOUND_ERROR;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    public static final Long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;        // accessToken 유효시간
    public static final Long REFRESH_TOKEN_EXPIRE_TIME = 12 * 60 * 60 * 1000L;  // refreshToken 유효시간

    public AuthResponse.Token signIn(AuthRequest.SignIn signIn) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성, 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = signIn.toAuthentication();
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        Authentication authentication = null;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new ApiException(USERNAME_NOT_FOUND_ERROR);
        }
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        PrivateClaims claims = PrivateClaims.of(user.getEmail(), user.getAuthorities());
        String accessToken = createToken(claims, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = createToken(claims, REFRESH_TOKEN_EXPIRE_TIME);
        return AuthResponse.Token.of(accessToken, refreshToken);
    }

    public boolean signOut() {
        SecurityContextHolder.clearContext();
        return true;
    }

    public AuthResponse.Token reissueToken(String refreshToken) {
        String userId = jwtProvider.getCurrentUserEmail();
        String storedRefreshToken = "";

        PrivateClaims privateClaims = jwtProvider.parseRefreshToken(refreshToken, storedRefreshToken);
        String newAccessToken = createToken(privateClaims, ACCESS_TOKEN_EXPIRE_TIME);
        String newRefreshToken = createToken(privateClaims, REFRESH_TOKEN_EXPIRE_TIME);

        return AuthResponse.Token.of(newAccessToken, newRefreshToken);
    }

    private String createToken(PrivateClaims privateClaims , Long expireTime) {
        return jwtProvider.generateToken(privateClaims, expireTime);
    }
}
