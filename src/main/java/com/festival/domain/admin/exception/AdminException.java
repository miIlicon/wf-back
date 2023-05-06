package com.festival.domain.admin.exception;

import com.festival.global.exception.ErrorCode;
import com.festival.global.exception.baseException;
import org.springframework.http.HttpStatus;

public class AdminException extends baseException {

    private static final ErrorCode code = ErrorCode.USER_NOT_FOUND;

    public AdminException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
