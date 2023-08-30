package com.festival.common.exception.custom_exception;

import com.festival.common.exception.CustomException;
import com.festival.common.exception.ErrorCode;

public class InvalidException extends CustomException {
    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
