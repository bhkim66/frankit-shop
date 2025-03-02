package com.frankit.shop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * The enum Exception enum.
 */
@Getter
@RequiredArgsConstructor
@ToString
public enum ExceptionEnum {

    // 상품 옵션
    OPTION_SIZE_OVER(HttpStatus.BAD_REQUEST, "OPT_400_01", "3개 이하의 상품 옵션만 등록할 수 있습니다."),
    OPTION_TYPE_IS_NOT_EQUALS(HttpStatus.BAD_REQUEST, "OPT_400_02", "기존 옵션 타입과 다른 옵션 타입을 등록할 수 없습니다."),

    ILLEGAL_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "GLO_400_01", "잘못된 인수값이 전달 됐습니다."),
    /** Validation 체크 */
    METHOD_ARGUMENT_NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "GLO_400_02", "잘못된 입력 값 입니다."),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "GLO_404_01", "요청한 요소를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLO_500_01", "예기치 못한 오류가 발생 했습니다."),
    IO_ARGUMENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLO_500_02", "잘못된 값 입니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;
}
