package com.festival.domain.admin.exception;

import com.festival.global.exception.ErrorCode;
import com.festival.global.exception.baseException;
import org.springframework.http.HttpStatus;

public class AdminNotMatchException extends baseException {
    private static final ErrorCode code = ErrorCode.ADMIN_NOT_MATCH;

    public AdminNotMatchException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
