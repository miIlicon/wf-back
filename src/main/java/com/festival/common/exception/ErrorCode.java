package com.festival.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400
    EMPTY_REFRESH_TOKEN("RefreshToken이 필요합니다.", HttpStatus.BAD_REQUEST),
    ALREADY_DELETED("이미 삭제된 글입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TYPE("입력된 값의 타입이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_LENGTH("입력된 값의 길이가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_RANGE("입력된 값의 범위가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_STATUS("입력된 값의 상태가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_DATE("입력된 값의 날짜 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL("입력된 값의 이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER("필수 파라미터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // 401
    LOGOUTED_TOKEN("이미 로그아웃 처리된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    SNATCH_TOKEN("Refresh Token 탈취를 감지하여 로그아웃 처리됩니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TYPE_TOKEN("Token의 타입은 Bearer입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_PERIOD_ACCESS_TOKEN("기한이 만료된 AccessToken입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_PERIOD_REFRESH_TOKEN("기한이 만료된 RefreshToken입니다.", HttpStatus.UNAUTHORIZED),
    EMPTY_AUTHORITY("권한 정보가 필요합니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("유효하지 않은 AccessToken입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("유효하지 않은 RefreshToken입니다.", HttpStatus.UNAUTHORIZED),

    // 404
    NOT_FOUND_BOOTH("부스가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_GUIDE("안내사항이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_PROGRAM("이벤트가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_BAMBOO("대나무숲글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_MEMBER("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_TIMETABLE("시간표가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_IMAGE("이미지가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_OBJECT("존재하지 않는 객체입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_OAUTH_PROVIDER("지원하지 않는 소셜 로그인 플랫폼 입니다.", HttpStatus.NOT_FOUND),

    // 403
    FORBIDDEN_UPDATE("수정 권한이 없습니다.", HttpStatus.FORBIDDEN),
    FORBIDDEN_DELETE("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 409
    DUPLICATION_ID("중복된 아이디가 입력되었습니다.", HttpStatus.CONFLICT);


    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
