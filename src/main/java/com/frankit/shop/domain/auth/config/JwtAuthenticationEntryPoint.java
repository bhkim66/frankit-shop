package com.frankit.shop.domain.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frankit.shop.global.common.ApiResponseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.frankit.shop.global.exception.ExceptionEnum.UNAUTHORIZED_MEMBER;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * 인증되지 않는 요청 오류 처리
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        setErrorResponse(response, authException);
    }

    private void setErrorResponse(HttpServletResponse res, RuntimeException e) throws IOException {
        ApiResponseResult<String> result = ApiResponseResult.failure(UNAUTHORIZED_MEMBER);
        ObjectMapper objectMapper = new ObjectMapper();
        String resultSrt = objectMapper.writeValueAsString(result);

        res.setContentType("application/json; charset=UTF-8");
        res.getWriter().write(resultSrt);
        res.setStatus(SC_UNAUTHORIZED);
        res.flushBuffer();
    }
}
