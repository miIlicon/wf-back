package com.festival.old.domain.info.festivalEvent.exception;

import com.festival.old.global.exception.ErrorCode;
import com.festival.old.global.exception.baseException;
import org.springframework.http.HttpStatus;

public class FestivalEventNotFoundException extends baseException {
    private static final ErrorCode code = ErrorCode.FESTIVALEVENT_NOT_FOUND;

    public FestivalEventNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
