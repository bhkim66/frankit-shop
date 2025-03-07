package com.frankit.shop.domain.auth.config;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.domain.auth.entity.CustomUserDetail;
import com.frankit.shop.domain.auth.entity.PrivateClaims;
import com.frankit.shop.domain.auth.handler.JwtHandler;
import com.frankit.shop.domain.user.entity.User;
import com.frankit.shop.global.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.frankit.shop.domain.auth.common.RoleEnum.ROLE_DEFAULT;
import static com.frankit.shop.global.exception.ExceptionEnum.INVALID_TOKEN_VALUE_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtHandler jwtHandler;
    private static final String USER_EMAIL = "email";
    private static final String USER_ROLE = "role";

    public String generateToken(PrivateClaims privateClaims, Long expireTime) {
        return jwtHandler.createJwt(Map.of(USER_EMAIL, privateClaims.getEmail(), USER_ROLE, privateClaims.getAuthority()), expireTime);
    }

    // 정합성 비교 후 값이 일치한다면 새로운 Claims 값을 리턴해줌
    public PrivateClaims parseRefreshToken(String requestRefreshToken, String storedRefreshToken) {
        if (!requestRefreshToken.equals(storedRefreshToken)) {
            throw new ApiException(INVALID_TOKEN_VALUE_ERROR);
        }
        return jwtHandler.parseClaims(requestRefreshToken).map(this::convert).orElseThrow();
    }

    // 토큰 정보를 검증하는 메서드
    public Authentication validateToken(String token) {
        try {
            jwtHandler.parseClaims(token);
            return getAuthentication(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("[validateTokenException] Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.error("[validateTokenException] Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.error("[validateTokenException] Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.error("[validateTokenException] JWT claims string is empty.");
        }
        return null;
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = jwtHandler.parseClaims(token).orElseThrow();
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" +
                claims.get(USER_ROLE)));

        // UserDetails 객체를 만들어서 Authentication 리턴
        CustomUserDetail principal = CustomUserDetail.of(User.of((String) claims.get(USER_EMAIL), ROLE_DEFAULT));
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private CustomUserDetail getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException(INVALID_TOKEN_VALUE_ERROR);
        }
        return (CustomUserDetail) authentication.getPrincipal();
    }

    public String getCurrentUserEmail() {
        return getCurrentUserDetails().getEmail();
    }


//    public String getRefreshTokenFromHeader() {
//        return validateToken(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
//                .getRequest().getHeader(HEADER_KEY_AUTHORIZATION));
//    }

    private PrivateClaims convert(Claims claims) {
        String email = PrivateClaims.convertType(claims.get(USER_EMAIL));
        Object rolesObject = claims.get(USER_ROLE);
        Set<RoleEnum> authorities = new HashSet<>();

        if (rolesObject instanceof List<?>) {
            authorities = ((List<?>) rolesObject).stream()
                    .map(Object::toString)
                    .map(RoleEnum::valueOf)
                    .collect(Collectors.toSet());
        }

        return PrivateClaims.of(email, authorities);
    }
}
