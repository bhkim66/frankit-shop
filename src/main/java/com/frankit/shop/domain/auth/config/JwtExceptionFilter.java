package com.frankit.shop.domain.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frankit.shop.global.common.ApiResponseResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.springframework.http.HttpStatus.*;

/**
 * Security 인증/인가 과정에서 생기는 오류 처리
 */
@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private void setErrorResponse(HttpServletResponse res, Exception e) throws IOException {
        ApiResponseResult<String> result = ApiResponseResult.failure(String.valueOf(INTERNAL_SERVER_ERROR.value()), e.getMessage());
        String resultSrt = objectMapper.writeValueAsString(result);

        res.setContentType("application/json; charset=UTF-8");
        res.getWriter().write(resultSrt);
        res.setStatus(SC_BAD_REQUEST);
        res.flushBuffer();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        try {
            filterChain.doFilter(request, response); // go to 'JwtAuthenticationFilter'
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
    }
}