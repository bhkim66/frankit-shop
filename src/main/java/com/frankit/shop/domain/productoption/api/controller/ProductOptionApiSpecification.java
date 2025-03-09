package com.frankit.shop.domain.productoption.api.controller;

import com.frankit.shop.domain.productoption.dto.ProductOptionRequest;
import com.frankit.shop.domain.productoption.dto.ProductOptionResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "product option api", description = "상품 옵션 api 설명입니다.")
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
public interface ProductOptionApiSpecification {
    @Operation(summary = "상품 옵션 조회", description = "특정 상품에 존재하는 옵션들을 조회할 수 있다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(
                                    name = "successExample",
                                    value = """
                                            {
                                                "success": true,
                                                "data": [
                                                    {
                                                        "id": 1,
                                                        "optionName": "FREE",
                                                        "optionType": "S",
                                                        "extraPrice": 0
                                                    }
                                                ],
                                                "error": null
                                            }"""
                            ),
                            @ExampleObject(
                                    name = "noDataExample",
                                    value = """
                                            {
                                                "success": true,
                                                "data": {
                                                    "content": []
                                                },
                                                "error": null
                                            }"""
                            )
                    }
            )),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "badRequestExample",
                                    value = """
                                            {
                                                "success": false,
                                                "data": null,
                                                "error": {
                                                    "errorMessage": "GLO_400_01",
                                                    "errorCode": "잘못된 인수값이 전달 됐습니다."
                                                }
                                            }""")
                    }))
    })
    ResponseEntity<ApiResponseResult<List<ProductOptionResponse>>> selectProductOptions(@PathVariable(value = "productId") Long productId);

    @Operation(summary = "상품 옵션 추가", description = "특정 상품에 3개 이하의 옵션을 추가할 수 있다")
    @Parameters({
            @Parameter(name = "name", description = "상품 옵션 이름" , schema = @Schema(type = "string")),
            @Parameter(name = "type", description = "상품 옵션 타입", schema = @Schema(type = "string")),
            @Parameter(name = "extraPrice", description = "옵션 추가 금액", schema = @Schema(type = "integer")),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
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
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "forbiddenExample",
                                    value = """
                                            {
                                                "success": false,
                                                "data": null,
                                                "error": {
                                                    "errorMessage": "AUT_403_01",
                                                    "errorCode": "권한이 없는 멤버의 접근입니다."
                                                }
                                            }""")
                    }))
    })
    ResponseEntity<ApiResponseResult<Void>> addProductOptions(@PathVariable("productId") Long productId, @RequestBody List<ProductOptionRequest> productOptionRequests);

    @Operation(summary = "상품 옵션 수정", description = "특정 상품에 옵션들을 수정할 수 있다")
    @Parameters({
            @Parameter(name = "name", description = "상품 옵션 이름" , schema = @Schema(type = "string")),
            @Parameter(name = "type", description = "상품 옵션 타입", schema = @Schema(type = "string")),
            @Parameter(name = "extraPrice", description = "옵션 추가 금액", schema = @Schema(type = "integer")),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
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
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "forbiddenExample",
                                    value = """
                                            {
                                                "success": false,
                                                "data": null,
                                                "error": {
                                                    "errorMessage": "AUT_403_01",
                                                    "errorCode": "권한이 없는 멤버의 접근입니다."
                                                }
                                            }""")
                    }))
    })
    ResponseEntity<ApiResponseResult<Void>> updateProductOption(@PathVariable("id") Long id, @RequestBody ProductOptionRequest productOptionRequest);

    @Operation(summary = "상품 옵션 삭제", description = "특정 상품에 옵션들을 삭제할 수 있다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
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
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "forbiddenExample",
                                    value = """
                                            {
                                                "success": false,
                                                "data": null,
                                                "error": {
                                                    "errorMessage": "AUT_403_01",
                                                    "errorCode": "권한이 없는 멤버의 접근입니다."
                                                }
                                            }""")
                    }))
    })
    ResponseEntity<ApiResponseResult<Void>> deleteProductOption(@PathVariable("id") Long id);
}
