package com.festival.common.exception.custom_exception;

import com.festival.common.exception.CustomException;
import com.festival.common.exception.ErrorCode;

public class ForbiddenException extends CustomException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
