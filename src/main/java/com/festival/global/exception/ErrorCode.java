package com.festival.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ADMIN_NOT_FOUND(404, "ADMIN-001", "관리자가 존재하지 않는 경우"),
    ADMIN_NOT_MATCH(403, "ADMIN-002", "관리자 권한이 다를 경우"),
    MAXSIZE_OVER(400, "ADMIN-003", String.format("파일은 최대 %sMB 크기까지 입력할 수 있습니다.", 3)),
    PUB_NOT_FOUND(404, "PUB-001", "주점이 존재하지 않는 경우"),
    PUB_CONTENT_NOT_FOUND(404, "PUB-002", "주점의 이미지가 존재하지 않는 경우"),
    FESTIVALEVENT_NOT_FOUND(404, "FESTIVALEVENT-001", "축제 이벤트가 존재하지 않는 경우"),
    FESTIVALEVENT_IMAGE_NOT_FOUND(404, "FESTIVALEVENT-002", "축제 이벤트 이미지가 존재하지 않는 경우"),
    FILM_NOT_FOUND(404, "Film-001", "축제 이벤트가 존재하지 않는 경우"),
    FLEA_MARKET_NOT_FOUND(404, "FLEA_MARKET-001", "플리마켓이 존재하지 않는 경우");

    private final int status;
    private final String code;
    private final String description;

    ErrorCode(int status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
}
