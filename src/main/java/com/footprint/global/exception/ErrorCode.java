package com.footprint.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT(400, "잘못된 입력입니다."),
    UNAUTHORIZED(401, "인증이 필요합니다."),
    FORBIDDEN(403, "접근 권한이 없습니다."),
    NOT_FOUND(404, "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다."),

    // User
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(409, "이미 존재하는 이메일입니다."),

    // Walk
    WALK_NOT_FOUND(404, "산책 기록을 찾을 수 없습니다."),

    // Footprint
    FOOTPRINT_NOT_FOUND(404, "발자국을 찾을 수 없습니다."),

    // Place
    PLACE_NOT_FOUND(404, "장소를 찾을 수 없습니다."),

    // Auth
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "만료된 토큰입니다.");

    private final int status;
    private final String message;
}
