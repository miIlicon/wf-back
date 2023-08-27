package com.festival.old.domain.info.festivalPub.exception;

import com.festival.old.global.exception.ErrorCode;
import com.festival.old.global.exception.baseException;
import org.springframework.http.HttpStatus;

public class PubNotFoundException extends baseException {

    private static final ErrorCode code = ErrorCode.PUB_NOT_FOUND;

    public PubNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}