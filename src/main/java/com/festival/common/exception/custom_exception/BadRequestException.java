package com.festival.common.exception.custom_exception;

import com.festival.common.exception.CustomException;
import com.festival.common.exception.ErrorCode;

public class BadRequestException extends CustomException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}