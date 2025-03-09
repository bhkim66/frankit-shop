package com.frankit.shop.domain.auth.api.controller;

import com.frankit.shop.domain.auth.dto.AuthRequest;
import com.frankit.shop.domain.auth.dto.AuthResponse;
import com.frankit.shop.global.common.ApiResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "auth api", description = "인증 인가 api 설명입니다.")
@ApiResponse(responseCode = "500", description = "서버 응답 실패", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
        examples = {
                @ExampleObject(name = "internalServerExample",
                        value = """
                                {
                                    "success": false,
                                    "data": null,
                                    "error": {
                                        "errorMessage": "GLO_500_01",
                                        "errorCode": "예기치 못한 오류가 발생 했습니다."
                                    }
                                }"""
                )
        }))
public interface AuthApiSpecification {

    @Operation(summary = "로그인", description = "유저 정보로 로그인을 할 수 있다")
    @Parameters({
            @Parameter(name = "email", description = "계정 이메일 형태로 넘어와야한다", schema = @Schema(type = "string")),
            @Parameter(name = "password", description = "계정 패스워드" , schema = @Schema(type = "string")),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "successExample",
                                    value = """
                                            {
                                                "success": true,
                                                "data": null,
                                                "error": null
                                            }""")
                    })),
            @ApiResponse(responseCode = "400", description = "필수값 입력 누락", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "badRequestExample",
                                    value = """
                                            {
                                                "success": false,
                                                "data": null,
                                                "error": {
                                                    "errorMessage": "GLO_400_02",
                                                    "errorCode": "입력값 필수입니다."
                                                }
                                            }""")
                    }))
    })
    ResponseEntity<ApiResponseResult<AuthResponse.Token>> signIn(@RequestBody @Valid AuthRequest.SignIn signIn);

    @Operation(summary = "로그아웃", description = "로그아웃을 한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "successExample",
                                    value = """
                                            {
                                                "success": true,
                                                "data": null,
                                                "error": null
                                            }""")
            }))
    })
    ResponseEntity<ApiResponseResult<Boolean>> signOut();

    @Operation(summary = "인증 정보 재발급", description = "RefreshToken으로 AccessToken을 재발급 받을 수 있다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재발급 성공", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "successExample",
                                    value = """
                                            {
                                                "success": true,
                                                "data": {
                                                    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjpbIlVTRVIiXSwiZW1haWwiOiJ1c2VydGVzdDEyM0BuYXZlci5jb20iLCJleHAiOjE3NDE1MDE3Mzl9.xzbeAdspYiAz_AXZN66jwam-ERxRwo5j0Z_CKBb7zXs",
                                                    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjpbIlVTRVIiXSwiZW1haWwiOiJ1c2VydGVzdDEyM0BuYXZlci5jb20iLCJleHAiOjE3NDE1NDMxMzl9.ucrs3BGLfNRZTaDG1YHSQCdRp755ONJlP9Fug7PzyFU",
                                                    "publishTime": "2025-03-09T14:58:59.5017918"
                                                },
                                                "error": null
                                            }""")
                    })),
            @ApiResponse(responseCode = "400", description = "필수값 입력 누락", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "badRequestExample",
                                    value = """
                                            {
                                                "success": false,
                                                "data": null,
                                                "error": {
                                                    "errorMessage": "GLO_400_02",
                                                    "errorCode": "입력값 필수입니다."
                                                }
                                            }""")
                    })),
            @ApiResponse(responseCode = "400", description = "유효하지 않는 토큰 값", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "badRequestExample",
                                    value = """
                                            {
                                                "success": false,
                                                "data": null,
                                                "error": {
                                                    "errorMessage": "AUT_400_01",
                                                    "errorCode": "유효하지 않은 토큰 입니다."
                                                }
                                            }""")
                    })),
            @ApiResponse(responseCode = "401", description = "인증 정보 누락", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "unauthorizedExample",
                                    value = """
                                            {
                                                "success": false,
                                                "data": null,
                                                "error": {
                                                    "errorMessage": "AUT_401_01",
                                                    "errorCode": "인증되지 않은 멤버 접근입니다."
                                                }
                                            }""")
                    }))
    })
    ResponseEntity<ApiResponseResult<AuthResponse.Token>> reissueToken(@RequestHeader(value = "Authorization") String refreshToken);

}