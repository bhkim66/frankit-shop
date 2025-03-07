package com.frankit.shop.domain.auth.api.controller;

import com.frankit.shop.domain.auth.api.service.AuthService;
import com.frankit.shop.domain.auth.dto.AuthRequest;
import com.frankit.shop.domain.auth.dto.AuthResponse;
import com.frankit.shop.global.common.ApiResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponseResult<AuthResponse.Token>> signIn(@RequestBody @Valid AuthRequest.SignIn signIn) {
        return ResponseEntity.ok(ApiResponseResult.success(authService.signIn(signIn)));
    }

    @GetMapping("/sign-out")
    public ResponseEntity<ApiResponseResult<Boolean>> signOut() {
        return ResponseEntity.ok(ApiResponseResult.success(authService.signOut()));
    }

    @PostMapping("/reissue-token")
    public ResponseEntity<ApiResponseResult<AuthResponse.Token>> reissueToken(@RequestHeader(value = "Authorization") String refreshToken) {
        return ResponseEntity.ok(ApiResponseResult.success(authService.reissueToken(refreshToken)));
    }
}
