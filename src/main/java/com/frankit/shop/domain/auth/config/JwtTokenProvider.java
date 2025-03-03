package com.frankit.shop.domain.auth.config;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.domain.auth.entity.CustomUserDetail;
import com.frankit.shop.domain.auth.handler.JwtHandler;
import com.frankit.shop.domain.user.entity.User;
import com.frankit.shop.global.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static com.frankit.shop.domain.auth.common.ConstDef.HEADER_KEY_AUTHORIZATION;
import static com.frankit.shop.global.exception.ExceptionEnum.INVALID_TOKEN_VALUE_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtHandler jwtHandler;
    private static final String USER_ID = "userId";
    private static final String ROLE = "role";

    @Getter
    @AllArgsConstructor
    public static class PrivateClaims {
        private String userId;
        private RoleEnum role;
    }

    public String generateToken(PrivateClaims privateClaims, Long expireTime) {
        return jwtHandler.createJwt(Map.of(USER_ID, privateClaims.getUserId(), ROLE, privateClaims.getRole()), expireTime);
    }

    //토큰 재발급에서 쓰임 - Refresh Token이 유효한지 확인
    public PrivateClaims parseRefreshToken(String requestRefreshToken, String storedRefreshToken) {
        if (!requestRefreshToken.equals(storedRefreshToken)) {
            throw new ApiException(INVALID_TOKEN_VALUE_ERROR);
        }
        return jwtHandler.parseClaims(requestRefreshToken).map(this::convert).orElseThrow();
    }

    // 토큰 정보를 검증하는 메서드
    public String validateToken(String token) {
        try {
            Optional<Claims> claims = jwtHandler.parseClaims(token);
            return token;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("[validateTokenException] Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.error("[validateTokenException] Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.error("[validateTokenException] Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.error("[validateTokenException] JWT claims string is empty.");
        }
        throw new ApiException(INVALID_TOKEN_VALUE_ERROR);
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = jwtHandler.parseClaims(token).orElseThrow();
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" +
                claims.get(ROLE)));

        User user = User.of((String) claims.get(USER_ID), RoleEnum.valueOf((String) claims.get(ROLE)));

        // UserDetails 객체를 만들어서 Authentication 리턴
        CustomUserDetail principal = new CustomUserDetail(user);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String getRefreshTokenFromHeader() {
        return validateToken(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader(HEADER_KEY_AUTHORIZATION));
    }

    private PrivateClaims convert(Claims claims) {
        return new PrivateClaims(claims.get(USER_ID, String.class), RoleEnum.valueOf(claims.get(ROLE, String.class)));

//    public Set<RoleEnum> extractUserRole() {
//        return getUserDetail().getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .map(RoleEnum::valueOf)
//                .collect(Collectors.toSet());
//    }
//
//    private CustomUserDetail getUserDetail() {
//        return (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }
    }
}
