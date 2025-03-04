package com.frankit.shop.domain.auth.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtHandler {
    private final Key key;
    public JwtHandler() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public String createJwt(Map<String, Object> privateClaims, Long expireTime) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .setIssuedAt(now) // 토큰 발급 시간
                .setClaims(privateClaims)
                .setExpiration(expireDate) // 만료 시간
                .signWith(key) // 사용 암호 알고리즘
                .compact();
    }

    public Optional<Claims> parseClaims(String token) {
        return Optional.of(Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody());
    }
}
