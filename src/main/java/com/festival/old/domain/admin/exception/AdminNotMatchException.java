package com.festival.old.domain.admin.exception;

import com.festival.old.global.exception.ErrorCode;
import com.festival.old.global.exception.baseException;
import org.springframework.http.HttpStatus;

public class AdminNotMatchException extends baseException {
    private static final ErrorCode code = ErrorCode.ADMIN_NOT_MATCH;

    public AdminNotMatchException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
