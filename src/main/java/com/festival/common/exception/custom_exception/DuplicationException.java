package com.festival.common.exception.custom_exception;

import com.festival.common.exception.CustomException;
import com.festival.common.exception.ErrorCode;

public class DuplicationException extends CustomException {

    public DuplicationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
