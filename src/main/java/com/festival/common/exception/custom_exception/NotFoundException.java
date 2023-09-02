package com.festival.common.exception.custom_exception;

import com.festival.common.exception.CustomException;
import com.festival.common.exception.ErrorCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
