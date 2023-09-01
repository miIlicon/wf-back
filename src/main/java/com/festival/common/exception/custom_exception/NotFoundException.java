package com.festival.common.exception.custom_exception;

import com.festival.common.exception.ErrorCode;

public class NotFoundException extends BadRequestException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
