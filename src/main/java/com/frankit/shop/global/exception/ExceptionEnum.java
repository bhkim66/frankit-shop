package com.frankit.shop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * The enum Exception enum.
 */
@Getter
@RequiredArgsConstructor
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

    /** 토큰이 유효하지 않을 때 or 로그아웃 된 토큰으로 인증 요청할 때 */
    INVALID_TOKEN_VALUE_ERROR(HttpStatus.UNAUTHORIZED, "AUT_400_01", "유효하지 않은 토큰 입니다."),
    /** 인증되지 않은 멤버 접근 */
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "AUT_401_01", "인증되지 않은 멤버 접근입니다."),
    /** 권한이 없는 멤버 접근 */
    ACCESS_DENIED_MEMBER(HttpStatus.FORBIDDEN, "AUT_403_01", "권한이 없는 멤버의 접근입니다."),
    USERNAME_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "AUT_404_01", "존재하지 않는 사용자입니다."),

    /** REDIS ERROR */
    REDIS_TOKEN_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "RED_404_01", "요청한 요소를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;
}
