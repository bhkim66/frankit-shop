package com.frankit.shop.domain.product.api.controller;

import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.dto.ProductResponse;
import com.frankit.shop.global.common.ApiResponseResult;
import com.frankit.shop.global.condition.ProductCondition;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "product api", description = "상품 api 설명입니다.")
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
public interface ProductApiSpecification {
    @Operation(summary = "상품 목록 조회", description = "여러개의 검색 조건과 함께 상품을 페이징 조회힌다")
    @Parameters({
            @Parameter(name = "name", description = "상품 이름", schema = @Schema(type = "string")),
            @Parameter(name = "createdAt", description = "상품 생성 날짜", schema = @Schema(type = "string")),
            @Parameter(name = "minPrice", description = "검색시 최소 금액", schema = @Schema(type = "integer")),
            @Parameter(name = "maxPrice", description = "검색시 최대 금액", schema = @Schema(type = "integer")),
            @Parameter(name = "page", description = "페이지 번호", schema = @Schema(type = "integer")),
            @Parameter(name = "size", description = "(선택적) 페이지당 컨텐츠 개수", schema = @Schema(type = "integer"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(
                                    name = "successExample",
                                    value = """
                                            "success": true,
                                                "data": {
                                                    "content": [
                                                        {
                                                            "id": 1,
                                                            "name": "m4 pro",
                                                            "description": "애플 노트북 입니다",
                                                            "price": 1100000,
                                                            "deliveryFee": 5000,
                                                            "createdAt": null,
                                                            "options": [
                                                                {
                                                                    "id": 1,
                                                                    "optionName": "FREE",
                                                                    "optionType": "S",
                                                                    "extraPrice": 0
                                                                }
                                                            ]
                                                        }\
                                                         ],
                                                    "page": {
                                                        "size": 5,
                                                        "number": 0,
                                                        "totalElements": 5,
                                                        "totalPages": 1
                                                    }
                                                },
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
    ResponseEntity<ApiResponseResult<Page<ProductResponse>>> selectProductsWithCondition(ProductCondition condition, Pageable page);

    @Operation(summary = "상품 조회", description = "하나의 상품을 조회힌다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponseResult.class),
                    examples = {
                            @ExampleObject(name = "successExample",
                                    value = """
                                            {
                                                "success": true,
                                                "data": {
                                                    "id": 1,
                                                    "name": "컴퓨터1",
                                                    "description": "컴퓨터입니다",
                                                    "price": 1100000,
                                                    "deliveryFee": 5000,
                                                    "createdAt": null,
                                                    "options": [
                                                        {
                                                            "id": 1,
                                                            "optionName": "FREE",
                                                            "optionType": "S",
                                                            "extraPrice": 0
                                                        }
                                                    ]
                                                },
                                                "error": null
                                            }"""),
                            @ExampleObject(name = "noDataExample",
                                    value = """
                                            {
                                                "success": false,
                                                "data": null,
                                                "error": {
                                                    "errorMessage": "GLO_404_01",
                                                    "errorCode": "요청한 요소를 찾을 수 없습니다."
                                                }
                                            }""")
                    })),
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
    ResponseEntity<ApiResponseResult<ProductResponse>> selectProduct(@PathVariable("id") Long productId);

    @Operation(summary = "상품 생성", description = "하나의 상품을 생성한다")
    @Parameters({
            @Parameter(name = "name", description = "상품 이름", schema = @Schema(type = "string")),
            @Parameter(name = "description", description = "상품 설명", schema = @Schema(type = "string")),
            @Parameter(name = "price", description = "상품 금액", schema = @Schema(type = "integer")),
            @Parameter(name = "deliveryFee", description = "배송비", schema = @Schema(type = "integer")),
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
    ResponseEntity<ApiResponseResult<Void>> createProducts(@RequestBody @Valid ProductRequest request);

    @Operation(summary = "상품 수정", description = "하나의 상품을 수정한다")
    @Parameters({
            @Parameter(name = "name", description = "상품 이름", schema = @Schema(type = "string")),
            @Parameter(name = "description", description = "상품 설명", schema = @Schema(type = "string")),
            @Parameter(name = "price", description = "상품 금액", schema = @Schema(type = "integer")),
            @Parameter(name = "deliveryFee", description = "배송비", schema = @Schema(type = "integer")),
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
    ResponseEntity<ApiResponseResult<Void>> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest request);

    @Operation(summary = "상품 삭제", description = "하나의 상품을 삭제한다")
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
    ResponseEntity<ApiResponseResult<Void>> deleteProduct(@PathVariable Long id);
}
