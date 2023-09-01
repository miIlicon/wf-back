package com.festival.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 400
    ALREADY_DELETED("이미 삭제된 글입니다.", HttpStatus.BAD_REQUEST),

    // 404
    NOT_FOUND_BOOTH("부스가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_GUIDE("안내사항이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_PROGRAM("이벤트가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_BAMBOO("대나무숲글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_MEMBER("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_TIMETABLE("시간표가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_IMAGE("이미지가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

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
