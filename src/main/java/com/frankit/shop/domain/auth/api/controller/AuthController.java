package com.frankit.shop.domain.auth.api.controller;

import com.frankit.shop.domain.auth.api.service.AuthService;
import com.frankit.shop.domain.auth.dto.AuthRequest;
import com.frankit.shop.domain.auth.dto.AuthResponse;
import com.frankit.shop.global.common.ApiResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponseResult<AuthResponse.Token>> signIn(@RequestBody AuthRequest.SignIn signIn) {
        return ResponseEntity.ok(ApiResponseResult.success(authService.signIn(signIn)));
    }

//    @GetMapping("/sign-out")
//    public ResponseEntity<ApiResponseResult<Void>> signOut() {
//        return ResponseEntity.ok(userService.signOut());
//    }
//
//    @PostMapping("/reissueToken")
//    public ResponseEntity<ApiResponseResult<AuthResponse.Token>> reissueToken(HttpServletRequest request) {
//        return ResponseEntity.ok(ApiResponseResult.success(userService.reissueToken()));
//    }
//
//    @PutMapping("/update")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<ApiResponseResult<Boolean>> updateUser(@RequestBody @Valid UserRequestDTO.UpdateUserInfo updateUserInfo, @AuthenticationPrincipal CustomUserDetail user) {
//        return ResponseEntity.ok(userService.updateUser(updateUserInfo, user.getId()));
//    }
//
//    @PutMapping("/change-password")
//    public ResponseEntity<ApiResponseResult<Boolean>> changePassword(@RequestBody @Valid UserRequestDTO.UpdatePassword updatePassword, @AuthenticationPrincipal CustomUserDetail user) {
//        return ResponseEntity.ok(userService.changePassword(updatePassword, user.getId()));
//    }

}
