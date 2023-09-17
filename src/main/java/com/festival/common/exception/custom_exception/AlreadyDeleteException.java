package com.festival.common.exception.custom_exception;

import com.festival.common.exception.ErrorCode;

public class AlreadyDeleteException extends BadRequestException {
    public AlreadyDeleteException(ErrorCode errorCode) {
        super(errorCode);
    }
}
