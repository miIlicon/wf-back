package com.festival.domain.info.festivalPub.exception;

import com.festival.global.exception.ErrorCode;
import com.festival.global.exception.baseException;
import org.springframework.http.HttpStatus;

public class PubNotFoundException extends baseException {

    private static final ErrorCode code = ErrorCode.PUB_NOT_FOUND;

    public PubNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}