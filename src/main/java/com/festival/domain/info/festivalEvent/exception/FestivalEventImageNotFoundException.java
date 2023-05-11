package com.festival.domain.info.festivalEvent.exception;

import com.festival.global.exception.ErrorCode;
import com.festival.global.exception.baseException;
import org.springframework.http.HttpStatus;

public class FestivalEventImageNotFoundException extends baseException {
    private static final ErrorCode code = ErrorCode.FESTIVALEVENT_IMAGE_NOT_FOUND;

    public FestivalEventImageNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
